<%@ Page Language="C#" AutoEventWireup="true" CodeFile="TableDisplay.aspx.cs" Inherits="TableDisplay" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
    <div>
        <asp:LinkButton ID="LinkButton1" runat="server" onclick="LinkButton1_Click">Accounts</asp:LinkButton>
    <asp:GridView ID="GridView1" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton2" runat="server" onclick="LinkButton2_Click">Students</asp:LinkButton>
    <asp:GridView ID="GridView2" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton3" runat="server" onclick="LinkButton3_Click">Courses</asp:LinkButton>
    <asp:GridView ID="GridView3" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton4" runat="server" onclick="LinkButton4_Click">Assignments</asp:LinkButton>
    <asp:GridView ID="GridView4" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton5" runat="server" onclick="LinkButton5_Click">Grades</asp:LinkButton>
    <asp:GridView ID="GridView5" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton6" runat="server" onclick="LinkButton6_Click">Infractions</asp:LinkButton>
    <asp:GridView ID="GridView6" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton7" runat="server" onclick="LinkButton7_Click">Locations</asp:LinkButton>
    <asp:GridView ID="GridView7" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton8" runat="server" onclick="LinkButton8_Click">Meta</asp:LinkButton>
    <asp:GridView ID="GridView8" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton9" runat="server" onclick="LinkButton9_Click">Notifications</asp:LinkButton>
    <asp:GridView ID="GridView9" runat="server">
    </asp:GridView>
    <br />
    <asp:LinkButton ID="LinkButton10" runat="server" onclick="LinkButton9_Click">Messages</asp:LinkButton>
    <asp:GridView ID="GridView10" runat="server">
    </asp:GridView>
    </div>
    <br />
    <br />
    </form>
</body>
</html>
