# Spring Security with JWT Implementation

### Admin Endpoints

<table>
  <tr>
      <th>Method</th>
      <th>Url</th>
      <th>Description</th>
      <th>Request Body</th>
      <th>Header</th>
  </tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/admins/register</td>
      <td>Admin Register</td>
      <td>AdminRegistrationRequest</td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/admins/login</td>
      <td>Admin Login</td>
      <td>AdminLoginRequest</td>
      <td></td>
  <tr>
  <tr>
      <td>POST</td>
      <td>/api/v1/admins/refresh-token</td>
      <td>Admin Refresh Token</td>
      <td>HttpServletRequest request,
        HttpServletResponse response</td>
      <td>Bearer Token</td>
  <tr>
</table>

<style>
.table_component {
    overflow: auto;
    width: 100%;
}

.table_component table {
    border: 1px solid #dededf;
    height: 100%;
    width: 100%;
    table-layout: auto;
    border-collapse: collapse;
    border-spacing: 1px;
    text-align: left;
}

.table_component caption {
    caption-side: top;
    text-align: left;
}

.table_component th {
    border: 1px solid #dededf;
    background-color: #eceff1;
    color: #000000;
    padding: 5px;
}

.table_component td {
    border: 1px solid #dededf;
    background-color: #ffffff;
    color: #000000;
    padding: 5px;
}
</style>
<div class="table_component" role="region" tabindex="0">
<table>
    <caption>Table 1</caption>
    <thead>
        <tr>
            <th>Method</th>
            <th>Url</th>
            <th>Description</th>
            <th>Request Body</th>
            <th>Header</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST</td>
            <td>/api/v1/admins/register</td>
            <td>Admin Register</td>
            <td>AdminRegistrationRequest</td>
            <td></td>
        </tr>
        <tr>
            <td>POST</td>
            <td>/api/v1/admins/login</td>
            <td>Admin Login</td>
            <td>AdminLoginRequest</td>
            <td></td>
        </tr>
        <tr>
            <td>POST</td>
            <td>/api/v1/admins/refresh-token</td>
            <td>Admin Refresh Token</td>
            <td></td>
            <td>Bearer Token</td>
        </tr>
    </tbody>
</table>
<div style="margin-top:8px">Made with <a href="https://www.htmltables.io/" target="_blank">HTML Tables</a></div>
</div>
