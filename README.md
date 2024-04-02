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
