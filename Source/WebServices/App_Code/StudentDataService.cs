﻿using System;
using System.Collections;
using System.Linq;
using System.Web;
using System.Web.Services;
using System.Web.Services.Protocols;
using System.Xml.Linq;
using System.Configuration;
using System.Data.SqlClient;
using System.Collections.Generic;
using System.Web.Script.Services;



[Serializable]
public class Assignment
{
    public Assignment()
    {
        mAssignment = -1;
        mCourseID = -1;
        mName = "";
        mDescription = "";
        mPoints = 0.0F;
        mDueDate = DateTime.Now;
        mIsTest = true;
    }

    public Assignment
        (
        int aAssignment,
        int aCourseID,
        string aName,
        string aDesc,
        float aPoints,
        DateTime aDate,
        bool aTest
        )
    {
        mAssignment = aAssignment;
        mCourseID = aCourseID;
        mName = aName;
        mDescription = aDesc;
        mPoints = aPoints;
        mDueDate = aDate;
        mIsTest = aTest;
    }

    // Handle nullable input
    public Assignment
        (
        int? aAssignment,
        int? aCourseID,
        string aName,
        string aDesc,
        float? aPoints,
        DateTime? aDate,
        bool? aTest
        )
    {
        if (aAssignment.HasValue)
        {
            mAssignment = aAssignment.Value;
        }
        else
        {
            mAssignment = -1;
        }

        if (aCourseID.HasValue)
        {
            mCourseID = aCourseID.Value;
        }
        else
        {
            mCourseID = -1;
        }
        mName = aName;
        mDescription = aDesc;

        if (aPoints.HasValue)
        {
            mPoints = aPoints.Value;
        }
        else
        {
            mPoints = 0.0F;
        }

        if (aDate.HasValue)
        {
            mDueDate = aDate.Value;
        }
        else
        {
            mDueDate = DateTime.Now;
        }

        if (aTest.HasValue)
        {
            mIsTest = aTest.Value;
        }
        else
        {
            mIsTest = true;
        }
    }

	// Correspond to C# equivalents of the Assignments columns
    public int mAssignment;		
    public int mCourseID;
    public string mName;
    public string mDescription;
    public float mPoints;
    public DateTime mDueDate;
    public bool mIsTest;
}

[Serializable]
public class Infraction
{
    public Infraction()
    {
        mInfractionID = -1;
        mStudentID = -1;
        mInfractionType = StudentDataService.INFRACTION_TYPE.INFRACTION_COUNT;
        mDescription = "";
        mDate = DateTime.Now;
    }

    public Infraction
        (
        int aInfractionID,
        int aStudentID,
        StudentDataService.INFRACTION_TYPE aInfractionType,
        string aDesc,
        DateTime aDate
        )
    {
        mInfractionID = aInfractionID;
        mStudentID = aStudentID;
        mInfractionType = aInfractionType;
        mDescription = aDesc;
        mDate = aDate;
    }

    // Handle nullable input
    public Infraction
        (
        int? aInfractionID,
        int? aStudentID,
        StudentDataService.INFRACTION_TYPE? aInfractionType,
        string aDesc,
        DateTime? aDate
        )
    {
        mInfractionID = aInfractionID.HasValue ? aInfractionID.Value : -1;
        mStudentID = aStudentID.HasValue ? aStudentID.Value : -1;
        mInfractionType = aInfractionType.HasValue ? aInfractionType.Value : StudentDataService.INFRACTION_TYPE.INFRACTION_COUNT;
        mDescription = aDesc;
        mDate = aDate.HasValue ? aDate.Value : DateTime.Now;
    }

	// Correspond to C# equivalents of the Infractions columns
    public int mInfractionID;
    public int mStudentID;
    public StudentDataService.INFRACTION_TYPE mInfractionType;
    public string mDescription;
    public DateTime mDate;
}

[Serializable]
public class Grade
{
    public Grade()
    {
        mGradeID = -1;
        mAssignmentID = -1;
        mStudentID = -1;
        mPointsEarned = 0.0F;
        mDateSubmitted = DateTime.Now;
    }

    public Grade
        (
        int aGradeID,
        int aAssignmentID,
        int aStudentID,
        double aPoints,
        DateTime aDate
        )
    {
        mGradeID = aGradeID;
        mAssignmentID = aAssignmentID;
        mStudentID = aStudentID;
        mPointsEarned = aPoints;
        mDateSubmitted = aDate;
    }

    // Handle nullable input
    public Grade
        (
        int? aGradeID,
        int? aAssignmentID,
        int? aStudentID,
        double? aPoints,
        DateTime? aDate
        )
    {
        mGradeID = aGradeID.HasValue ? aGradeID.Value : -1;
        mAssignmentID = aAssignmentID.HasValue ? aAssignmentID.Value : -1;
        mStudentID = aStudentID.HasValue ? aStudentID.Value : -1;
        mPointsEarned = aPoints.HasValue ? aPoints.Value : -1.0;
        mDateSubmitted = aDate.HasValue ? aDate.Value : DateTime.Now;
    }

	// Correspond to C# equivalents of the Grades columns
    public int mGradeID;
    public int mAssignmentID;
    public int mStudentID;
    public double mPointsEarned;
    public DateTime mDateSubmitted;
}


/// <summary>
/// Summary description for StudentDataService
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
[ScriptService]
[GenerateScriptType(typeof(Infraction))]
[GenerateScriptType(typeof(Grade))]
[GenerateScriptType(typeof(Assignment))]
public class StudentDataService : System.Web.Services.WebService
{

    public enum INFRACTION_TYPE
    {
        INFRACTION_TARDY = 0,
        INFRACTION_ABSENT,
        INFRACTION_DETENTION,
        INFRACITON_SUSPENDED,
        INFRACTION_EXPELLED,

        INFRACTION_COUNT
    };

    public StudentDataService()
    {
    }

    [WebMethod]
    public bool addClass
        (
        int aTeacherID,		// Teacher ID to verify and to have teaching the class
        string aPassword,	// Teacher's password
        string aClassName	// Name of the class to add
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        bool success = verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            //Get new class ID
            int classID = generateID(AccountService.ID_TYPE.COURSE_ID);

            // Add the class
            success = generateGenericInsertCommand(conn,
                                                    "Courses",
                                                    classID.ToString(),
                                                    aTeacherID.ToString(),
                                                    aClassName);
        }

        conn.Close();

        return success;
    }

    [WebMethod]
    public bool addStudentToClass
        (
        int aTeacherID,		// Teacher ID used to verify
        string aPassword,	// Teacher's password
        int aClassID,		// Class ID to add student to
        int aStudentID		// Student to add
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        bool success = verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            SqlCommand classCmd = new SqlCommand("SELECT class1, class2, class3 " +
                                                 "FROM Students " +
                                                 "WHERE id = " + aStudentID
                                                , conn);
            SqlDataReader reader = classCmd.ExecuteReader();
            reader.Read();

            int count = reader.FieldCount;
            int?[] classes = new int?[count];

            for (int j = 0; j < count; j++)
            {
                classes[j] = reader[j] as int?;
            }

            reader.Dispose();
            classCmd.Dispose();

            int i = 0;
            for( ; i < count; i++ )
            {
                if( !classes[i].HasValue )
                {
                    break;
                }
            }

            if( i < count )
            {
                string columnName = "class";
                columnName += (i+1).ToString();

                using( SqlCommand cmd = new SqlCommand("UPDATE Students " +
                                                        "SET " + columnName + " = " + aClassID, conn ) )
                {
                    cmd.ExecuteNonQuery();
                }
            }
            else
            {
                success = false;
            }
            

        }

        conn.Close();

        return success;
    }


    [WebMethod]
    // TODO: This may want to get the names of them instead of the IDs (maybe?)
    public List<int> getClasses
        (
        int aStudentID	// Student ID to get classes for
        )
    {

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        List<int> classes = new List<int>();

        string query = "SELECT class1, class2, class3 " +
                       "FROM Students " +
                       "WHERE id = " + aStudentID;

        using (SqlCommand cmd = new SqlCommand(query, conn))
        {
            SqlDataReader reader = cmd.ExecuteReader();
            reader.Read();

            for (int i = 0; i < reader.FieldCount; i++)
            {
                int? cls = reader[i] as int?;
                if (cls.HasValue)
                {
                    classes.Add(cls.Value);
                }
            }

            reader.Dispose();

        }

        conn.Close();

        return classes;
    }

    [WebMethod]
    public bool addAssignment
        (
        int aTeacherID,		// Teacher ID to verify
        string aPassword,	// Password for teacher to verify
        int aClassID,		// Class ID to add assignment to
        float aTotalPoints, // Total points the assignment will be worth
        bool aTest,			// Is it a test?
        DateTime aDateDue,	// Date it is due
        string aName,		// Name of the assignment
        string aDescription	// Description of the assignment
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();


        bool success = verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            // Get new ID
            int assignmentID = generateID(AccountService.ID_TYPE.ASSIGNMENT_ID);

            // Add the assignment
            success = generateGenericInsertCommand(conn,
                                                    "Assignments",
                                                    assignmentID.ToString(),
                                                    aClassID.ToString(),
                                                    aName,
                                                    aDescription,
                                                    aTotalPoints.ToString(),
                                                    aDateDue.ToString(),
                                                    aTest.ToString());

        }

        conn.Close();

        return success;

    }

    [WebMethod]
    public List<Assignment> getAssignments
        (
        int aClassID	// Class ID to get assignments for
        )
    {
        List<Assignment> assignments = new List<Assignment>();

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        SqlCommand assignCmd = new SqlCommand("SELECT * " +
                                             "FROM Assignments " +
                                             "WHERE courseID = " + aClassID
                                            , conn);

        SqlDataReader reader = assignCmd.ExecuteReader();
        while (reader.Read())
        {
            int? assignID = reader["assignmentID"] as int?;
            int? courseID = reader["courseID"] as int?;
            string name = reader["name"] as string;
            string description = reader["description"] as string;
            float? totalPoints = reader["totalPoints"] as float?;
            DateTime? dueDate = reader["dueDate"] as DateTime?;
            bool? isTest = reader["isTest"] as bool?;

            Assignment assign = new Assignment(assignID, courseID, name, description, totalPoints, dueDate, isTest);
            assignments.Add(assign);
        }

        reader.Dispose();
        assignCmd.Dispose();

        conn.Close();

        return assignments;
    }

    [WebMethod]
    // TODO: Make addGrades for more convenience?
    public bool addGrade
        (
        int aTeacherID,				// Teacher ID to verify
        string aPassword,			// Password for teacher to verify
        int aStudentID,				// Student ID to add grade for
        int aAssignmentID,			// Assignment ID to add grade to
        double aPointsReceived,		// Points the student received
        DateTime aDateSubmitted		// Date the student submitted it
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        bool success = verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            // Get new ID
            int gradeID = generateID(AccountService.ID_TYPE.GRADE_ID);

            // Add the assignment
            success = generateGenericInsertCommand(conn,
                                                    "Grades",
                                                    gradeID.ToString(),
                                                    aAssignmentID.ToString(),
                                                    aStudentID.ToString(),
                                                    aPointsReceived.ToString(),
                                                    aDateSubmitted.ToString());
        }

        conn.Close();

        return success;
    }

    [WebMethod]
    public List<Grade> getGrades
        (
        int aStudentID,	// Student ID to get grades for
        int aClassID	// class ID to get grades for
        )
    {
        List<Grade> grades = new List<Grade>();

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        // Get list of assignments for this class
        List<int> classAssignments = new List<int>();
        string assignQ = "SELECT assignmentID FROM Assignments WHERE courseID = " + aClassID;
        using (SqlCommand cmd = new SqlCommand(assignQ, conn))
        {
            SqlDataReader reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                classAssignments.Add((int)reader[0]);
            }
            reader.Dispose();
        }

        using( SqlCommand assignCmd = new SqlCommand("SELECT * " +
                                        "FROM Grades " +
                                        "WHERE studentID = " + aStudentID
                                    , conn) )
        {
            SqlDataReader reader = assignCmd.ExecuteReader();
            while (reader.Read())
            {
                // Make sure the assignment is for this class
                if( classAssignments.Contains( (int)reader["assignmentID"] ) )
                {
                    int? assignID = reader["gradeID"] as int?;
                    int? courseID = reader["assignmentID"] as int?;
                    int? studentID = reader["studentID"] as int?;
                    double? pointsEarned = reader["pointsEarned"] as double?;
                    DateTime? dateSubmitted = reader["dateSubmitted"] as DateTime?;

                    Grade grade = new Grade(assignID, courseID, studentID, pointsEarned, dateSubmitted);
                    grades.Add(grade);
                }
            }

            reader.Dispose();
        }

        conn.Close();

        return grades;
    }

    [WebMethod]
    // TODO: Infractions in ParentalManagement?
    public bool addInfraction
        (
        int aTeacherID,						// Teacher ID to verify
        string aPassword,					// Password to verify
        int aStudentID,						// Student ID to add infraction to
        INFRACTION_TYPE aInfractionType,	// Type of infraction
        string aDescription,				// Description of infraction
        DateTime aDate						// Date the infraction occurred
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabase"].ConnectionString);
        conn.Open();

        bool success = verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            // Get new ID
            int infractionID = generateID(AccountService.ID_TYPE.INFRACTION_ID);

            // Add the assignment
            // TODO: Add time field to infractions
            success = generateGenericInsertCommand(conn,
                                                    "Infractions",
                                                    infractionID.ToString(),
                                                    aStudentID.ToString(),
                                                    aInfractionType.ToString(),
                                                    aDescription,
                                                    aDate.ToString());

        }

        conn.Close();

        return success;
    }

    [WebMethod]
    public List<Infraction> getInfractions
        (
        int aStudentID	// Student ID to get infractions for
        )
    {
        // TODO
        List<Infraction> infractions = new List<Infraction>();
        Infraction tmp = new Infraction(1, 1, INFRACTION_TYPE.INFRACTION_ABSENT, "", DateTime.Now);
        infractions.Add(tmp);
        return infractions;
    }

    [WebMethod]
    public int getParentID
        (
        int aStudentID	// Student ID to get the parent for
        )
    {
        return getLinkedID(aStudentID, AccountService.ACCOUNT_TYPE.STUDENT);
    }

    [WebMethod]
    public int getStudentID
        (
        int aParentID	// Parent ID to get student for
        )
    {
        return getLinkedID(aParentID, AccountService.ACCOUNT_TYPE.PARENT);
    }

    protected int getLinkedID
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
        if (aIDType == (AccountService.ACCOUNT_TYPE)type.Value)
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

    protected bool verifyTeacher
        (
        int aTeacherID,				// Teacher ID to verify
        string aPassword,			// Teacher's password
        SqlConnection aConnection	// Connection to use. Caller must open and close it
        )
    {
        // Find teacher email using the ID
        SqlCommand emailCmd = new SqlCommand("SELECT emailAddress " +
                                             "FROM Accounts " +
                                             "WHERE id = " + aTeacherID
                                            , aConnection);
        string emailAddress = (string)emailCmd.ExecuteScalar();
        emailCmd.Dispose();

        // Verify that the password is correct for this teacher
        AccountService accountService = new AccountService();

        bool success = false;
        if ("Login Successful" == accountService.Login(emailAddress, aPassword))
        {
            success = true;
        }

        accountService.Dispose();

        return success;
    }

    // Assume first is the table name
    // Returns whether the write was successful
    protected bool generateGenericInsertCommand
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

    protected int generateID
        (
        AccountService.ID_TYPE aID	// ID type to generate 
        )
    {
        AccountService accountService = new AccountService();
        int id = accountService.generateNewID(aID);
        accountService.Dispose();
        return id;
    }

}

