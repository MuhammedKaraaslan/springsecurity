# Spring Security with JWT Implementation

### Admin Endpoints

  <table style="border: 1px solid #dededf; height: 100%; width: 100%; table-layout: auto; border-collapse: collapse; border-spacing: 1px; text-align: left;">
        <caption style="caption-side: top; text-align: left;">Table 1</caption>
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
        <caption style="caption-side: top; text-align: left;">Table 1</caption>
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
