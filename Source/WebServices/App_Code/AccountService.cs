using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.Services;

[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
[System.Web.Script.Services.ScriptService]
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
     *  Service Methods
    *************************************************************/

    [WebMethod]
    public string Register( string name, string email, string password, string passwordRepeat, int type, string realName )
    {
        /* Check user doesn't exist in database */
        if( AccountExists( email ) )
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
                                              " values('"+ id + "','" + type + "','" + -1 + "','" + email + "','" + name + "','" + password + "','" + realName + "')", conn);
        writeCmd.ExecuteNonQuery();
        writeCmd.Dispose();

        // If it's a student account, also add them to the Students table
        if (ACCOUNT_TYPE.STUDENT == (ACCOUNT_TYPE)type)
        {
            // 'using' disposes of the command afterward
            using (SqlCommand cmd = new SqlCommand("INSERT INTO Students(id)" +
                                              "VALUES('"+ id + "')"
                                              , conn))
            {

                cmd.ExecuteNonQuery();
            }
        }


        conn.Close();

        // Note:  This should return additional information to help link accounts as a parent
        return "Registration Successful";
    }


    [WebMethod]
    public string Login(string email, string password)
    {
        /* Check user exists */
        if (  !AccountExists( email )  )
            return "Error logging in:  Invalid email.";

        /* Check user credentials */
        string pass = getPassword( email );
        if ( password != pass )
            return "Error logging in:  Invalid password.";

        return "Login Successful";
    }


    [WebMethod]
    public string LinkAccounts( string parentEmail, string studentEmail )
    {

        /* Check users exist in database */
        if (   !AccountExists(parentEmail)
            || !AccountExists(studentEmail)
           )
            return "Error:  an account does not exist.";

        /* Check user credentials */
        int type = getAccountType(parentEmail);
        if ( (ACCOUNT_TYPE)type != ACCOUNT_TYPE.PARENT )
            return "Error:  first account must be guardian type";
        type = getAccountType(studentEmail);
        if ( (ACCOUNT_TYPE)type != ACCOUNT_TYPE.STUDENT )
            return "Error:  second account must be student type";

        //TODO:  add security to enforce that links aren't formed to other parents' children

        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Link student to parent */
        int linkID = getAccountID(parentEmail);
        SqlCommand linkStudent = new SqlCommand("UPDATE Accounts SET linkedID='" + linkID + "' WHERE emailAddress='" + studentEmail + "'", conn);
        linkStudent.ExecuteNonQuery();
        linkStudent.Dispose();

        /* Linke parent to student */
        linkID = getAccountID(studentEmail);
        SqlCommand linkParent = new SqlCommand("UPDATE Accounts SET linkedID='" + linkID + "' WHERE emailAddress='" + parentEmail + "'", conn);
        linkParent.ExecuteNonQuery();
        linkParent.Dispose();

        conn.Close();

        return "Accounts linked successfully.";
    }


    /*************************************************************
     *  Protected Methods
     *************************************************************/

    protected bool AccountExists(string email)
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Check users exist in database */
        SqlCommand scanUsers = new SqlCommand("SELECT count(*) FROM Accounts WHERE emailAddress='" + email + "'", conn);
        int count = Convert.ToInt32(scanUsers.ExecuteScalar());
        scanUsers.Dispose();
        conn.Close();

        if (count == 0)
            return false;
        return true;
    }

    protected string getPassword(string email)
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Get account info  */
        SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn);
        SqlDataReader reader = getInfo.ExecuteReader();

        /* Read password */
        reader.Read();
        string pass = reader["password"].ToString();
        reader.Close();
        reader.Dispose();
        getInfo.Dispose();
        conn.Close();

        return pass;
    }

    protected int getAccountID(string email)
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Get account info  */
        SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn);
        SqlDataReader reader = getInfo.ExecuteReader();

        /* Read ID */
        reader.Read();
        int id = (int)reader["id"];
        reader.Close();
        reader.Dispose();
        getInfo.Dispose();
        conn.Close();

        return id;
    }

    protected int getAccountType(string email)
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Get account info  */
        SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn);
        SqlDataReader reader = getInfo.ExecuteReader();

        /* Read account type */
        reader.Read();
        int type = (int)reader["accountType"];
        reader.Close();
        reader.Dispose();
        getInfo.Dispose();
        conn.Close();

        return type;
    }

    /*************************************************************
     *  Public Helper Methods
     *************************************************************/
    public int generateNewID( ID_TYPE type )
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
        SqlCommand getMeta = new SqlCommand("SELECT " + idStr + " FROM Meta", conn);
        SqlDataReader reader = getMeta.ExecuteReader();
        reader.Read();
        int idNum = (int)reader[ idStr ];
        reader.Dispose();
        reader.Close();
        getMeta.Dispose();

        /* Update meta data */
        idNum++;
        SqlCommand setMeta = new SqlCommand("UPDATE Meta SET " + idStr + "='" + idNum + "'" , conn);
        setMeta.ExecuteNonQuery();
        setMeta.Dispose();

        conn.Close();
        return idNum;
    }

}
