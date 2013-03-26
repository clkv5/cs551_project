<%@ Page Title="Home Page" Language="C#" MasterPageFile="~/Site.master" AutoEventWireup="true"
    CodeFile="Default.aspx.cs" Inherits="_Default" %>

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>
<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
    <p>
        Web Services:<br />
        <asp:HyperLink ID="HyperLink1" runat="server" 
            NavigateUrl="~/AccountService.asmx">AccountService</asp:HyperLink>
        <br />
        <asp:HyperLink ID="HyperLink2" runat="server" 
            NavigateUrl="~/StudentDataService.asmx">StudentDataService</asp:HyperLink>
        <br />
        <asp:HyperLink ID="HyperLink3" runat="server" 
            NavigateUrl="~/ParentalManagementService.asmx">ParentalManagementService</asp:HyperLink>
    </p>
    <p>
        Debug:<br />
        <asp:HyperLink ID="HyperLink4" runat="server" NavigateUrl="~/TableDisplay.aspx">TableDisplay</asp:HyperLink>
        <br />
    </p>
</asp:Content>
