using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Configuration;
using System.Data.SqlClient;
using System.Data;

public partial class TableDisplay : System.Web.UI.Page
{
    string[] TABLE_NAMES = 
    {
        "Accounts",
        "Students",
        "Courses",
        "Assignments",
        "Grades",
        "Infractions",
        "Locations",
        "Meta",
        "Notifications",
		"Texts"
    };



    protected void Page_Load(object sender, EventArgs e)
    {
        // Only do this on the first load
        if (!Page.IsPostBack)
        {
            GridView[] TABLES =
            {
                GridView1,
                GridView2,
                GridView3,
                GridView4,
                GridView5,
                GridView6,
                GridView7,
                GridView8,
                GridView9,
				GridView10
            };


            SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["ProjectDatabaseString"].ConnectionString);
            conn.Open();

            for (int i = 0; i < TABLE_NAMES.Length; i++)
            {
                SqlDataAdapter da = new SqlDataAdapter("SELECT * " +
                                                       "FROM " + TABLE_NAMES[i],
                                                       conn);
                //Execute the select statement
                DataTable dt = new DataTable();
                da.Fill(dt);

                //Specify Gridview datasource
                TABLES[i].DataSource = dt;

                //bind data to grid view
                TABLES[i].DataBind();
                da.Dispose();
                conn.Close();

                TABLES[i].Visible = false;
            }
        }

    }

    protected void LinkButton1_Click(object sender, EventArgs e)
    {
        GridView1.Visible = !GridView1.Visible;
    }
    protected void LinkButton2_Click(object sender, EventArgs e)
    {
        GridView2.Visible = !GridView2.Visible;
    }
    protected void LinkButton3_Click(object sender, EventArgs e)
    {
        GridView3.Visible = !GridView3.Visible;
    }
    protected void LinkButton4_Click(object sender, EventArgs e)
    {
        GridView4.Visible = !GridView4.Visible;
    }
    protected void LinkButton5_Click(object sender, EventArgs e)
    {
        GridView5.Visible = !GridView5.Visible;
    }
    protected void LinkButton6_Click(object sender, EventArgs e)
    {
        GridView6.Visible = !GridView6.Visible;
    }
    protected void LinkButton7_Click(object sender, EventArgs e)
    {
        GridView7.Visible = !GridView7.Visible;
    }
    protected void LinkButton8_Click(object sender, EventArgs e)
    {
        GridView8.Visible = !GridView8.Visible;
    }
    protected void LinkButton9_Click(object sender, EventArgs e)
    {
        GridView9.Visible = !GridView9.Visible;
    }
	
	protected void LinkButton10_Click(object sender, EventArgs e)
    {
        GridView10.Visible = !GridView10.Visible;
    }
}