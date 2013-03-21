using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Xml.Linq;
using System.Web.Script.Services;


[Serializable]
public class Account
{
    public int id;
    public int accountType;
    public int linkedID;
    public string emailAddress;
    public string password;
    public string realName;

    public Account
        (
        int? acctID,
        int? type,
        int? linkID,
        string email,
        string pass,
        string name
        )
    {
        id = acctID.HasValue ? acctID.Value : -1;
        accountType = type.HasValue ? type.Value : -1;
        linkedID = linkID.HasValue ? linkID.Value : -1;
        emailAddress = email;
        password = pass;
        realName = name;
    }

    // Do not use default constructor!
    public Account()
    {
    }

}


[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
[ScriptService]
[GenerateScriptType(typeof(Account))]
public class AccountService : System.Web.Services.WebService {


    /*************************************************************
     *  Class Variables
    *************************************************************/
    public enum ID_TYPE
    {
        ACCOUNT_ID=0,
        COURSE_ID,
        ASSIGNMENT_ID,
        GRADE_ID,
        INFRACTION_ID,
        LOCATION_ID,
        NOTIFICATION_ID,

        ID_TYPE_COUNT
    };

    public enum ACCOUNT_TYPE
    {
        PARENT=0,
        STUDENT,
        STAFF
    };


    public AccountService ()
    {
    }

    /*************************************************************
     *  Web Service Methods
    *************************************************************/

    [WebMethod]
    public string Register
		(
		string email,			// Email address
		string password,		// Password
		string passwordRepeat,	// Password confirmation
        string realName,        // Real name
		int type				// Type of account to create
		)
    {
        /* Check user doesn't exist in database */
        if( accountExists( email ) )
            return "Error registering:  An account already exists with the given email.";

        /* Check user credentials */
        if( password != passwordRepeat )
            return "Error registering:  Password fields do not match.";

        /* Open connection to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Add new account */
        int id = generateNewID( ID_TYPE.ACCOUNT_ID );
        SqlCommand writeCmd = new SqlCommand("INSERT INTO Accounts " +
                                              " values('" + id + "','" + type + "','" + -1 + "','" + email + "','" + password + "','" + realName + "')", conn);
        writeCmd.ExecuteNonQuery();
        writeCmd.Dispose();

        /* If it's a student account, also add them to the Students table */
        if (ACCOUNT_TYPE.STUDENT == (ACCOUNT_TYPE)type)
        {
            using (SqlCommand cmd = new SqlCommand("INSERT INTO Students(id)" + "VALUES('"+ id + "')", conn) )
            {
                cmd.ExecuteNonQuery();
            }
        }

        conn.Close();

        // Note:  This should return additional information to help link accounts as a parent
        return "Registration Successful";
    }


    [WebMethod]
    public List<Account> Login
		(
		string email,		// Email address for the account
		string password		// Password
		)
    {
        /* Check user exists */
        if ( !accountExists( email ) )
            return null;

        /* Check user credentials */
        if( !checkCredentials(email, password) )
            return null;

        /* Retrieve account info */
        List<Account> account = new List<Account>();

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn);
        SqlDataReader reader = getInfo.ExecuteReader();
        reader.Read();

        int? id = reader["id"] as int?;
        int? type = reader["accountType"] as int?;
        int? lid = reader["linkedID"] as int?;
        string name = reader["realName"] as string;

        reader.Close();
        reader.Dispose();
        getInfo.Dispose();

        /* Return account data in XML */
        Account info = new Account( id, type, lid, email, password, name );
        account.Add(info);
        return account;
    }


    [WebMethod]
    public string LinkAccounts
		(
		string parentEmail,		// Email address for the parent's account to link
		string parentPassword,	// Password for the parent
		string studentEmail,	// Email address for the student's account to link
		string studentPassword	// Password for the student
		)
    {

        /* Check users exist in database */
        if( !accountExists(parentEmail) )
            return "Error:  parent account does not exist.";
        if (!accountExists(studentEmail) )
            return "Error:  student account does not exist.";
            

        /* Check user credentials */
        int type = getAccountType(parentEmail);
        if ( (ACCOUNT_TYPE)type != ACCOUNT_TYPE.PARENT )
            return "Error:  first account must be guardian type";
        type = getAccountType(studentEmail);
        if ( (ACCOUNT_TYPE)type != ACCOUNT_TYPE.STUDENT )
            return "Error:  second account must be student type";

        if( !checkCredentials(parentEmail, parentPassword) )
            return "Error:  invalid parent password";
        if( !checkCredentials(studentEmail, studentPassword) )
            return "Error:  invalid student passwor";

        //TODO:  add security to enforce that links aren't formed to other parents' children

        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Link student to parent */
        int linkID = getAccountID(parentEmail);
        using (SqlCommand linkStudent = new SqlCommand("UPDATE Accounts SET linkedID='" + linkID + "' WHERE emailAddress='" + studentEmail + "'", conn))
        {
            linkStudent.ExecuteNonQuery();
            linkStudent.Dispose();
        }

        /* Linke parent to student */
        linkID = getAccountID(studentEmail);
        using (SqlCommand linkParent = new SqlCommand("UPDATE Accounts SET linkedID='" + linkID + "' WHERE emailAddress='" + parentEmail + "'", conn))
        {
            linkParent.ExecuteNonQuery();
            linkParent.Dispose();
        }

        conn.Close();
        return "Accounts linked successfully.";
    }


    /*************************************************************
    *  Public Helper Methods
    *************************************************************/
    public static int generateNewID
		(
		ID_TYPE type	// Type of table (corresponding with an enum value) to generate an ID for
		)
    {
        /* Local variables */
        string[] dbRef = 
        {
            "lastAccountID",
            "lastCourseID",
            "lastAssignmentID",
            "lastGradeID",
            "lastInfractionID",
            "lastLocationID",
            "lastNotificationID"
        };

        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Acquire meta data */
        string idStr = dbRef[(int)type];
        int    idNum;

        using (SqlCommand getMeta = new SqlCommand("SELECT " + idStr + " FROM Meta", conn) )
        {
            SqlDataReader reader = getMeta.ExecuteReader();
            reader.Read();
            idNum = (int)reader[idStr];
            reader.Dispose();
            reader.Close();
        }

        /* Update meta data */
        idNum++;
        using (SqlCommand setMeta = new SqlCommand("UPDATE Meta SET " + idStr + "='" + idNum + "'", conn))
        {
            setMeta.ExecuteNonQuery();
        }

        conn.Close();
        return idNum;
    }

    public static int getLinkedID
        (
        int aID,
        AccountService.ACCOUNT_TYPE aIDType // The account type aID should be
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        SqlCommand cmd = new SqlCommand("SELECT * " +
                                        "FROM Accounts " +
                                        "WHERE id = " + (int)aID
                                        , conn);

        SqlDataReader reader = cmd.ExecuteReader();
        reader.Read();

        int? linkID = -1;

        int? type = reader["accountType"] as int?;

        // Verify that the ID is actually the correct type
        if (aIDType == (ACCOUNT_TYPE)type.Value)
        {
            linkID = reader["linkedID"] as int?;
        }

        reader.Dispose();
        cmd.Dispose();

        conn.Close();

        if (linkID.HasValue)
        {
            return linkID.Value;
        }

        return -1;
    }

    public static bool verifyTeacher
        (
        int aTeacherID,				// Account ID to verify
        string aPassword,			// Account's password
        SqlConnection aConnection	// Connection to use. Caller must open and close it
        )
    {
        string query = "SELECT accountType " +
                        "FROM Accounts " +
                        "WHERE id = " + aTeacherID;

        ACCOUNT_TYPE accountType = ACCOUNT_TYPE.STUDENT;
        using (SqlCommand cmd = new SqlCommand(query, aConnection))
        {
            accountType = (ACCOUNT_TYPE)cmd.ExecuteScalar();
        }

        bool success = false;
        if (ACCOUNT_TYPE.STAFF == accountType &&
             verifyAccount(aTeacherID, aPassword, aConnection))
        {
            success = true;
        }

        return success;
    }

    public static bool verifyAccount
        (
        int aTeacherID,				// Account ID to verify
        string aPassword,			// Account's password
        SqlConnection aConnection	// Connection to use. Caller must open and close it
        )
    {
        // Find email using the ID
        SqlCommand emailCmd = new SqlCommand("SELECT emailAddress " +
                                             "FROM Accounts " +
                                             "WHERE id = " + aTeacherID
                                            , aConnection);
        string emailAddress = (string)emailCmd.ExecuteScalar();
        emailCmd.Dispose();

        // Verify that the password is correct for this account
        AccountService service = new AccountService();

        bool success = false;
        if ("Login Successful" == service.Login(emailAddress, aPassword))
        {
            success = true;
        }

        service.Dispose();

        return success;
    }


    /*************************************************************
     *  Protected Methods
     *************************************************************/

    protected bool accountExists
        (
        string email    // Email address to check
        )
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Check users exist in database */
        int count;
        using (SqlCommand scanUsers = new SqlCommand("SELECT count(*) FROM Accounts WHERE emailAddress='" + email + "'", conn))
        {
            count = Convert.ToInt32(scanUsers.ExecuteScalar());
        }

        conn.Close();
        if (count == 0)
            return false;
        return true;
    }

    protected bool checkCredentials
        (
        string email,       // Email address to check
        string password     // Password to check
        )
    {
        string pass = getPassword(email);
        if (pass != password)
            return false;

        return true;
    }

    protected string getPassword
        (
        string email    // Email address to get password for
        )
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Get account info  */
        string pass;
        using (SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn))
        {
            SqlDataReader reader = getInfo.ExecuteReader();

            /* Read password */
            reader.Read();
            pass = reader["password"].ToString();
            reader.Close();
            reader.Dispose();
        }

        conn.Close();
        return pass;
    }

    protected int getAccountID
        (
        string email    // Email address to check
        )
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Get account info  */
        int id;
        using (SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn))
        {
            SqlDataReader reader = getInfo.ExecuteReader();

            /* Read ID */
            reader.Read();
            id = (int)reader["id"];
            reader.Close();
            reader.Dispose();
        }

        conn.Close();
        return id;
    }

    protected int getLinkedID
        (
        string email    // Email address to access
        )
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Get account info  */
        int id;
        using (SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn))
        {
            SqlDataReader reader = getInfo.ExecuteReader();

            /* Read ID */
            reader.Read();
            id = (int)reader["linkedID"];
            reader.Close();
            reader.Dispose();
        }

        conn.Close();
        return id;
    }

    protected int getAccountType
        (
        string email    // Email address to check
        )
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Get account info  */
        int type;
        using (SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn))
        {
            SqlDataReader reader = getInfo.ExecuteReader();

            /* Read account type */
            reader.Read();
            type = (int)reader["accountType"];
            reader.Close();
            reader.Dispose();
        }

        conn.Close();
        return type;
    }


}
