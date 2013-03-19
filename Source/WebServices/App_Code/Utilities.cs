using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.SqlClient;
using System.Configuration;

/// <summary>
/// Summary description for Utilities
/// </summary>
public class Utilities
{
	public Utilities()
	{
		//
		// TODO: Add constructor logic here
		//
	}

    // Assume first is the table name
    // Returns whether the write was successful
    public static bool generateGenericInsertCommand
        (
        SqlConnection aConnection,	// Connection to execute command with
        params string[] aList		// Variable length list of parameters
        )
    {
        int count = aList.Length;
        string command = "INSERT INTO " + aList[0] + " VALUES('";
        for (int i = 1; i < count; i++)
        {
            command += aList[i];
            command += (i == count - 1) ? "')" : "', '";
        }
        SqlCommand cmd = new SqlCommand(command, aConnection);
        bool ret = (cmd.ExecuteNonQuery() > 0);

        cmd.Dispose();
        return ret;
    }

}