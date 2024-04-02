# Spring Security with JWT Implementation

### Admin Endpoints

  <table height: 100%; width: 100%;">
        <thead>
            <tr>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Method</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Url</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Description</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Request Body</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Header</th>
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

  ### Customer Endpoints

  <table style="border: 1px solid #dededf; height: 100%; width: 100%; table-layout: auto; border-collapse: collapse; border-spacing: 1px; text-align: left;">
        <thead>
            <tr>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Method</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Url</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Description</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Request Body</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Header</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>POST</td>
                <td>/api/v1/customers/register</td>
                <td>Customer Register</td>
                <td>CustomerRegistrationRequest</td>
                <td></td>
            </tr>
            <tr>
                <td>POST</td>
                <td>/api/v1/customers/login</td>
                <td>Customer Login</td>
                <td>CustomerLoginRequest</td>
                <td></td>
            </tr>
            <tr>
                <td>POST</td>
                <td>/api/v1/customers/refresh-token</td>
                <td>Customer Refresh Token</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
        </tbody>
  </table>

  ### Demo Endpoints

  <table style="border: 1px solid #dededf; height: 100%; width: 100%; table-layout: auto; border-collapse: collapse; border-spacing: 1px; text-align: left;">
        <thead>
            <tr>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Method</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Url</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Description</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Request Body</th>
                <th style="border: 1px solid #dededf; background-color: #eceff1; color: #000000; padding: 5px;">Header</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>GET</td>
                <td>/api/v1/demo/public-endpoint</td>
                <td>Public Endpoint</td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td>GET</td>
                <td>/api/v1/demo/customer-secure-endpoint</td>
                <td>Customer Secure Endpoint</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
            <tr>
                <td>GET</td>
                <td>/api/v1/demo/admin-secure-endpoint</td>
                <td>Admin Secure Endpoint</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
             <tr>
                <td>GET</td>
                <td>/api/v1/demo/admin-or-customer-secure-endpoint</td>
                <td>Admin or Customer Secure Endpoint</td>
                <td></td>
                <td>Bearer Token</td>
            </tr>
        </tbody>
  </table>
