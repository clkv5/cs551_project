using System;
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
    public Assignment
        (
        SqlDataReader aReader   // SQL Data reader for a row from the Assignment table
        )
    {
        int? assignID = aReader["assignmentID"] as int?;
        int? courseID = aReader["courseID"] as int?;
        string name = aReader["name"] as string;
        string description = aReader["description"] as string;
        double? totalPoints = aReader["totalPoints"] as double?;
        DateTime? dueDate = aReader["dueDate"] as DateTime?;
        bool? isTest = aReader["isTest"] as bool?;

        mAssignment = assignID.HasValue ? assignID.Value : -1;
        mCourseID = courseID.HasValue ? courseID.Value : -1;
        mName = name;
        mDescription = description;
        mPoints = totalPoints.HasValue ? totalPoints.Value : 0.0F;
        mDueDate = dueDate.HasValue ? dueDate.Value : DateTime.Now;
        mIsTest = isTest.HasValue ? isTest.Value : true;
    }

    // Do not use default constructor!
    public Assignment()
    {
    }

	// Correspond to C# equivalents of the Assignments columns
    public int mAssignment;		
    public int mCourseID;
    public string mName;
    public string mDescription;
    public double mPoints;
    public DateTime mDueDate;
    public bool mIsTest;
}

[Serializable]
public class Infraction
{
    public Infraction
        (
        SqlDataReader aReader   // Reader to database to get info from
        )
    {
        int? aInfractionID = aReader["infractionID"] as int?;
        int? aStudentID = aReader["studentID"] as int?;
        StudentDataService.INFRACTION_TYPE? aInfractionType = aReader["infractionType"] as StudentDataService.INFRACTION_TYPE?;
        string aDesc = aReader["description"] as string;
        DateTime? aDate = aReader["date"] as DateTime?;

        mInfractionID = aInfractionID.HasValue ? aInfractionID.Value : -1;
        mStudentID = aStudentID.HasValue ? aStudentID.Value : -1;
        mInfractionType = aInfractionType.HasValue ? aInfractionType.Value : StudentDataService.INFRACTION_TYPE.INFRACTION_COUNT;
        mDescription = aDesc;
        mDate = aDate.HasValue ? aDate.Value : DateTime.Now;
    }

    // Do not use default constructor!
    public Infraction()
    {
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
    public Grade
        (
        SqlDataReader aReader   // Reader to get data from
        )
    {
        int? aGradeID = aReader["gradeID"] as int?;
        int? aAssignmentID = aReader["assignmentID"] as int?;
        int? aStudentID = aReader["studentID"] as int?;
        double? aPoints = aReader["pointsEarned"] as double?;
        DateTime? aDate = aReader["dateSubmitted"] as DateTime?;

        mGradeID = aGradeID.HasValue ? aGradeID.Value : -1;
        mAssignmentID = aAssignmentID.HasValue ? aAssignmentID.Value : -1;
        mStudentID = aStudentID.HasValue ? aStudentID.Value : -1;
        mPointsEarned = aPoints.HasValue ? aPoints.Value : -1.0;
        mDateSubmitted = aDate.HasValue ? aDate.Value : DateTime.Now;
    }

    // Do not use default constructor!
    public Grade()
    {
    }

	// Correspond to C# equivalents of the Grades columns
    public int mGradeID;
    public int mAssignmentID;
    public int mStudentID;
    public double mPointsEarned;
    public DateTime mDateSubmitted;
}

[Serializable]
public class Course
{
    public Course
        (
        SqlDataReader aReader   // Reader to get data from
        )
    {
        int? aCourseID = aReader["courseID"] as int?;
        int? aStaffID = aReader["staffID"] as int?;
        string aClassName = aReader["className"] as string;

        mCourseID = aCourseID.HasValue ? aCourseID.Value : -1;
        mStaffID = aStaffID.HasValue ? aStaffID.Value : -1;
        mClassName = aClassName;
    }

    // Do not use default constructor!
    public Course()
    {
    }

    // Correspond to C# equivalents of the Course columns
    public int mCourseID;
    public int mStaffID;
    public string mClassName;

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
[GenerateScriptType(typeof(Course))]
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
    // TODO: Make it possible to extend the number of classes
    // Do this by adding numClasses to the Meta table
    // Setting that adds "class1...numClasses" to the Students table (init to NULL)
    public bool addClass
        (
        int aTeacherID,		// Teacher ID to verify and to have teaching the class
        string aPassword,	// Teacher's password
        string aClassName	// Name of the class to add
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
        conn.Open();

        bool success = AccountService.verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            //Get new class ID
            int classID = AccountService.generateNewID(AccountService.ID_TYPE.COURSE_ID);

            // Add the class
            success = Utilities.generateGenericInsertCommand(conn,
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
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
        conn.Open();

        bool success = AccountService.verifyTeacher(aTeacherID, aPassword, conn);
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
    public List<Course> getTaughtClasses
        (
        int aTeacherID // Teacher ID to get classes for
        )
    {
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
        conn.Open();

        string query = "SELECT * " +
                       "FROM Courses " +
                       "WHERE staffID = " + aTeacherID;

        // Now get the class names
        List<Course> courses = new List<Course>();

        using (SqlCommand cmd = new SqlCommand(query, conn))
        {
            SqlDataReader reader = cmd.ExecuteReader();
            while (reader.Read())
            {
                Course assign = new Course(reader);
                courses.Add(assign);
            }

            reader.Dispose();
        }

        conn.Close();

        return courses;
    }

    [WebMethod]
    public List<Course> getClasses
        (
        int aStudentID	// Student ID to get classes for
        )
    {

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
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

        // Now get the class names
        List<Course> courses = new List<Course>();

        for (int i = 0; i < classes.Count; i++)
        {
            string query2 = "SELECT * " +
                            "FROM Courses " +
                            "WHERE courseID = " + classes[i];
            using (SqlCommand cmd = new SqlCommand(query2, conn))
            {
                SqlDataReader reader = cmd.ExecuteReader();
                while (reader.Read())
                {
                    Course assign = new Course(reader);
                    courses.Add(assign);
                }

                reader.Dispose();
            }
        }

        conn.Close();

        return courses;
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
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
        conn.Open();


        bool success = AccountService.verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            // Get new ID
            int assignmentID = AccountService.generateNewID(AccountService.ID_TYPE.ASSIGNMENT_ID);

            // Add the assignment
            success = Utilities.generateGenericInsertCommand(conn,
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

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
        conn.Open();

        SqlCommand assignCmd = new SqlCommand("SELECT * " +
                                             "FROM Assignments " +
                                             "WHERE courseID = " + aClassID
                                            , conn);

        SqlDataReader reader = assignCmd.ExecuteReader();
        while (reader.Read())
        {
            Assignment assign = new Assignment(reader);
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
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
        conn.Open();

        bool success = AccountService.verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            // Get new ID
            int gradeID = AccountService.generateNewID(AccountService.ID_TYPE.GRADE_ID);

            // Add the assignment
            success = Utilities.generateGenericInsertCommand(conn,
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

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
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
                    Grade grade = new Grade(reader);
                    grades.Add(grade);
                }
            }

            reader.Dispose();
        }

        conn.Close();

        return grades;
    }

    [WebMethod]
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
        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
        conn.Open();

        bool success = AccountService.verifyTeacher(aTeacherID, aPassword, conn);
        if (success)
        {
            // Get new ID
            int infractionID = AccountService.generateNewID(AccountService.ID_TYPE.INFRACTION_ID);

            // Add the assignment
            success = Utilities.generateGenericInsertCommand(conn,
                                                    "Infractions",
                                                    infractionID.ToString(),
                                                    aStudentID.ToString(),
                                                    ((int)aInfractionType).ToString(),
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
        List<Infraction> infractions = new List<Infraction>();

        SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
        conn.Open();

        SqlCommand assignCmd = new SqlCommand("SELECT * " +
                                             "FROM Infractions " +
                                             "WHERE studentID = " + aStudentID
                                            , conn);

        SqlDataReader reader = assignCmd.ExecuteReader();
        while (reader.Read())
        {
            Infraction assign = new Infraction(reader);
            infractions.Add(assign);
        }

        reader.Dispose();
        assignCmd.Dispose();

        conn.Close();

        return infractions;
    }

    [WebMethod]
    public int getParentID
        (
        int aStudentID	// Student ID to get the parent for
        )
    {
        return AccountService.getLinkedID(aStudentID, AccountService.ACCOUNT_TYPE.STUDENT);
    }

    [WebMethod]
    public int getStudentID
        (
        int aParentID	// Parent ID to get student for
        )
    {
        return AccountService.getLinkedID(aParentID, AccountService.ACCOUNT_TYPE.PARENT);
    }

}

