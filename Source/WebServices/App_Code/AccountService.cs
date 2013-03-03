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
public class WebService : System.Web.Services.WebService {


    /* Global variables */
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


    public WebService ()
    {
    }


    [WebMethod]
    public string Register( string name, string email, string password, string passwordRepeat )
    {
        /* Check user doesn't exist in database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        SqlCommand scanUsers = new SqlCommand("SELECT count(*) FROM Accounts WHERE emailAddress='" + email + "'", conn);
        int count = (int)scanUsers.ExecuteScalar();
        scanUsers.Dispose();

        if (count != 0)
            //return "Error registering:  An account already exists with the given email.";
            return count.ToString();

        /* Check user credentials */
        if( password != passwordRepeat )
            return "Error registering:  Password fields do not match.";

        /* Add user to database */
        int id = generateNewID( ID_TYPE.ACCOUNT_ID );
        //TODO:  handle accountType and linkedID
        SqlCommand writeCmd = new SqlCommand("INSERT INTO Accounts(id, accountType, linkedID, emailAddress, userName, password) " +
                                              " values('"+ id + "','" + 0 + "','" + id + "','" + email + "','" + name + "','" + password + "')", conn);
        writeCmd.ExecuteNonQuery();
        writeCmd.Dispose();

        conn.Close();
        return "Registration Successful";
    }


    [WebMethod]
    public string Login(string email, string password)
    {
        /* Connect to database */
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        /* Check user exists in database */
        SqlCommand scanUsers = new SqlCommand("SELECT count(*) FROM Accounts WHERE emailAddress='" + email + "'", conn);
        int count = Convert.ToInt32(scanUsers.ExecuteScalar());
        scanUsers.Dispose();

        if (count == 0)
            return "Error logging in:  Account does not exist";

        /* Acquire user info */
        SqlCommand getInfo = new SqlCommand("SELECT * FROM Accounts WHERE emailAddress='" + email + "'", conn);
        SqlDataReader reader = getInfo.ExecuteReader();

        reader.Read();
        string pass = reader["password"].ToString();
        reader.Close();
        reader.Dispose(); 
        getInfo.Dispose();

        /* Check user credentials */
        conn.Close();
        if (password != pass)
            return "Error logging in:  Invalid password";

        return "Login Successful";
    }


    protected int generateNewID( ID_TYPE type )
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
