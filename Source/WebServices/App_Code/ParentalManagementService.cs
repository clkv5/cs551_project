using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.Script.Services;
using System.Data.SqlClient;
using System.Configuration;

[Serializable]
public class Location
{
    public Location
        (
        SqlDataReader aReader   // Reader to get data from
        )
    {
        int? aLatitude = aReader["latitude"] as int?;
        int? aLongitude = aReader["longitude"] as int?;
        DateTime? aTime = aReader["time"] as DateTime?;

        mLatitude = aLatitude.HasValue ? aLatitude.Value : -1;
        mLongitude = aLongitude.HasValue ? aLongitude.Value : -1;
        mTime = aTime.HasValue ? aTime.Value : DateTime.Now;
    }

    // Do not use default constructor!
    public Location()
    {
    }

    // Correspond to C# equivalents of the Locations columns
    public int mLatitude;
    public int mLongitude;
    public DateTime mTime;

}

/// <summary>
/// Summary description for ParentalManagementService
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
[System.Web.Script.Services.ScriptService]
[GenerateScriptType(typeof(Location))]
public class ParentalManagementService : System.Web.Services.WebService {

    public ParentalManagementService () {
    }

    [WebMethod]
    public bool addLocation
        (
        int aStudentID,
        string aPassword,
        int aLatitude,
        int aLongitude,
        DateTime aTime
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        bool success = AccountService.verifyAccount(aStudentID, aPassword, conn);
        if (success)
        {
            // Get new ID
            int infractionID = AccountService.generateNewID(AccountService.ID_TYPE.LOCATION_ID);

            // Add the assignment
            success = Utilities.generateGenericInsertCommand(conn,
                                                    "Locations",
                                                    aLatitude.ToString(),
                                                    aLongitude.ToString(),
                                                    aStudentID.ToString(),
                                                    aTime.ToString());

        }

        conn.Close();

        return success;
    }

    [WebMethod]
    public List<Location> getLocations
        (
        int aStudentID,
        DateTime aStartTime,
        DateTime aEndTime
        )
    {
        List<Location> locations = new List<Location>();

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        SqlCommand assignCmd = new SqlCommand("SELECT * " +
                                             "FROM Locations " +
                                             "WHERE studentID = '" + aStudentID + "' " +
                                             "AND time <= '" + aEndTime + "' " +
                                             "AND time >= '" + aStartTime + "'"
                                            , conn);

        SqlDataReader reader = assignCmd.ExecuteReader();
        while (reader.Read())
        {
            Location location = new Location(reader);
            locations.Add(location);
        }

        reader.Dispose();
        assignCmd.Dispose();

        conn.Close();

        return locations;
    }
    
}
