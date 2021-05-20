//
//package statystech.aqaframework.common;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.restassured.RestAssured;
//import io.restassured.authentication.AuthenticationScheme;
//import io.restassured.builder.RequestSpecBuilder;
//import io.restassured.http.ContentType;
//import io.restassured.specification.RequestSpecification;
//import lombok.SneakyThrows;
//import org.apache.velocity.texen.Generator;
//import org.sonatype.plexus.components.sec.dispatcher.model.ConfigProperty;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.List;
//
//public class ApiClient {
//
//    private static final String url = ConfigProperty.getInstance().getTestRailApiUrl();
//    private static final String username = ConfigProperty.getInstance().getTestRailUsername();
//    private static final String apiKey = ConfigProperty.getInstance().getTestRailApiKey();
//    private RequestSpecification requestSpecification;
//    private AuthenticationScheme authenticationScheme;
//
//    private ObjectMapper mapper = new ObjectMapper();
//
//    public ApiClient() {
//        authenticationScheme = RestAssured.preemptive().basic(username, apiKey);
//        requestSpecification = new RequestSpecBuilder()
//                .setContentType(ContentType.JSON)
//                .setAuth(authenticationScheme)
//                .build();
//    }
//
//    //create new test run
//    @SneakyThrows
//    public GetRunDto addRun(int projectId, int suiteId, String releaseName) {
//        return RestAssured
//                .given(requestSpecification)
//                .body(mapper.writeValueAsString(buildAddRunDto(suiteId, releaseName)))
//                .post(url + "add_run/" + projectId)
//                .body()
//                .as(GetRunDto.class);
//    }
//
//    //update existing test run
//    @SneakyThrows
//    public GetResultsDto[] addResultsForCases(List<Integer> caseIds, int resultCode, int testRunId) {
//        return RestAssured
//                .given(requestSpecification)
//                .body(mapper.writeValueAsString(buildAddResultsForCasesDto(caseIds, resultCode)))
//                .post(url + "add_results_for_cases/" + testRunId)
//                .body()
//                .as(GetResultsDto[].class);
//    }
//
//    private AddRunDto buildAddRunDto(int suiteId, String releaseName) {
//        return AddRunDto.builder()
//                .suiteId(suiteId)
//                .name(Generator.generateTestRunName(releaseName))
//                .description("This test run was created automatically by autotests via statystech.aqaframework.steps.TestRail API")
//                .build();
//    }
//
//    private AddResultsForCasesDto buildAddResultsForCasesDto(List<Integer> caseIds, int resultCode) {
//        List<CaseResultDto> caseResultDtos = new ArrayList<>();
//        caseIds.stream().map(caseId -> CaseResultDto.builder()
//                .caseId(caseId)
//                .statusId(resultCode)
//                .build())
//                .forEach(caseResultDtos::add);
//
//        return AddResultsForCasesDto.builder()
//                .results(caseResultDtos)
//                .build();
//    }
//}
//	private String m_user;
//	private String m_password;
//	private String m_url;
//
//	public APIClient(String base_url)
//	{
//		if (!base_url.endsWith("/"))
//		{
//			base_url += "/";
//		}
//
//		this.m_url = base_url + "index.php?/api/v2/";
//	}
//
//	/**
//	 * Get/Set User
//	 *
//	 * Returns/sets the user used for authenticating the API requests.
//	 */
//	public String getUser()
//	{
//		return this.m_user;
//	}
//
//	public void setUser(String user)
//	{
//		this.m_user = user;
//	}
//
//	/**
//	 * Get/Set Password
//	 *
//	 * Returns/sets the password used for authenticating the API requests.
//	 */
//	public String getPassword()
//	{
//		return this.m_password;
//	}
//
//	public void setPassword(String password)
//	{
//		this.m_password = password;
//	}
//
//	/**
//	 * Send Get
//	 *
//	 * Issues a GET request (read) against the API and returns the result
//	 * (as Object, see below).
//	 *
//	 * Arguments:
//	 *
//	 * uri                  The API method to call including parameters
//	 *                      (e.g. get_case/1)
//	 *
//	 * Returns the parsed JSON response as standard object which can
//	 * either be an instance of JSONObject or JSONArray (depending on the
//	 * API method). In most cases, this returns a JSONObject instance which
//	 * is basically the same as java.util.Map.
//	 *
//	 * If 'get_attachment/:attachment_id', returns a String
//	 */
//	public Object sendGet(String uri, String data)
//		throws MalformedURLException, IOException, APIException
//	{
//		return this.sendRequest("GET", uri, data);
//	}
//
//	public Object sendGet(String uri)
//			throws MalformedURLException, IOException, APIException
//	{
//		return this.sendRequest("GET", uri, null);
//	}
//
//	/**
//	 * Send POST
//	 *
//	 * Issues a POST request (write) against the API and returns the result
//	 * (as Object, see below).
//	 *
//	 * Arguments:
//	 *
//	 * uri                  The API method to call including parameters
//	 *                      (e.g. add_case/1)
//	 * data                 The data to submit as part of the request (e.g.,
//	 *                      a map)
//	 *                      If adding an attachment, must be the path
//	 *                      to the file
//	 *
//	 * Returns the parsed JSON response as standard object which can
//	 * either be an instance of JSONObject or JSONArray (depending on the
//	 * API method). In most cases, this returns a JSONObject instance which
//	 * is basically the same as java.util.Map.
//	 */
//	public Object sendPost(String uri, Object data)
//		throws MalformedURLException, IOException, APIException
//	{
//		return this.sendRequest("POST", uri, data);
//	}
//
//	private Object sendRequest(String method, String uri, Object data)
//		throws MalformedURLException, IOException, APIException
//	{
//		URL url = new URL(this.m_url + uri);
//		// Create the connection object and set the required HTTP method
//		// (GET/POST) and headers (content type and basic auth).
//		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//
//		String auth = getAuthorization(this.m_user, this.m_password);
//		conn.addRequestProperty("Authorization", "Basic " + auth);
//
//		if (method.equals("POST"))
//		{
//			conn.setRequestMethod("POST");
//			// Add the POST arguments, if any. We just serialize the passed
//			// data object (i.e. a dictionary) and then add it to the
//			// request body.
//			if (data != null)
//			{
//				if (uri.startsWith("add_attachment"))   // add_attachment API requests
//				{
//					String boundary = "TestRailAPIAttachmentBoundary"; //Can be any random string
//					File uploadFile = new File((String)data);
//
//					conn.setDoOutput(true);
//					conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//
//					OutputStream ostreamBody = conn.getOutputStream();
//					BufferedWriter bodyWriter = new BufferedWriter(new OutputStreamWriter(ostreamBody));
//
//					bodyWriter.write("\n\n--" + boundary + "\r\n");
//					bodyWriter.write("Content-Disposition: form-data; name=\"attachment\"; filename=\""
//							+ uploadFile.getName() + "\"");
//					bodyWriter.write("\r\n\r\n");
//					bodyWriter.flush();
//
//					//Read file into request
//					InputStream istreamFile = new FileInputStream(uploadFile);
//					int bytesRead;
//					byte[] dataBuffer = new byte[1024];
//					while ((bytesRead = istreamFile.read(dataBuffer)) != -1)
//					{
//						ostreamBody.write(dataBuffer, 0, bytesRead);
//					}
//
//					ostreamBody.flush();
//
//					//end of attachment, add boundary
//					bodyWriter.write("\r\n--" + boundary + "--\r\n");
//					bodyWriter.flush();
//
//					//Close streams
//					istreamFile.close();
//					ostreamBody.close();
//					bodyWriter.close();
//				}
//				else	// Not an attachment
//				{
//					conn.addRequestProperty("Content-Type", "application/json");
//					byte[] block = JSONValue.toJSONString(data).
//						getBytes("UTF-8");
//
//					conn.setDoOutput(true);
//					OutputStream ostream = conn.getOutputStream();
//					ostream.write(block);
//					ostream.close();
//				}
//			}
//		}
//		else	// GET request
//		{
//			conn.addRequestProperty("Content-Type", "application/json");
//		}
//
//		// Execute the actual web request (if it wasn't already initiated
//		// by getOutputStream above) and record any occurred errors (we use
//		// the error stream in this case).
//		int status = conn.getResponseCode();
//
//		InputStream istream;
//		if (status != 200)
//		{
//			istream = conn.getErrorStream();
//			if (istream == null)
//			{
//				throw new APIException(
//					"statystech.aqaframework.steps.TestRail API return HTTP " + status +
//					" (No additional error message received)"
//				);
//			}
//		}
//		else
//		{
//			istream = conn.getInputStream();
//		}
//
//        // If 'get_attachment' (not 'get_attachments') returned valid status code, save the file
//        if ((istream != null)
//	    && (uri.startsWith("get_attachment/")))
//    	{
//            FileOutputStream outputStream = new FileOutputStream((String)data);
//
//            int bytesRead = 0;
//            byte[] buffer = new byte[1024];
//            while ((bytesRead = istream.read(buffer)) > 0)
//            {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            outputStream.close();
//            istream.close();
//            return (String) data;
//        }
//
//        // Not an attachment received
//		// Read the response body, if any, and deserialize it from JSON.
//		String text = "";
//		if (istream != null)
//		{
//			BufferedReader reader = new BufferedReader(
//				new InputStreamReader(
//					istream,
//					"UTF-8"
//				)
//			);
//
//			String line;
//			while ((line = reader.readLine()) != null)
//			{
//				text += line;
//				text += System.getProperty("line.separator");
//			}
//
//			reader.close();
//		}
//
//		Object result;
//		if (!text.equals(""))
//		{
//			result = JSONValue.parse(text);
//		}
//		else
//		{
//			result = new JSONObject();
//		}
//
//		// Check for any occurred errors and add additional details to
//		// the exception message, if any (e.g. the error message returned
//		// by statystech.aqaframework.steps.TestRail).
//		if (status != 200)
//		{
//			String error = "No additional error message received";
//			if (result != null && result instanceof JSONObject)
//			{
//				JSONObject obj = (JSONObject) result;
//				if (obj.containsKey("error"))
//				{
//					error = '"' + (String) obj.get("error") + '"';
//				}
//			}
//
//			throw new APIException(
//				"statystech.aqaframework.steps.TestRail API returned HTTP " + status +
//				"(" + error + ")"
//			);
//		}
//
//		return result;
//	}
//
//	private static String getAuthorization(String user, String password)
//	{
//		try
//		{
//			return new String(Base64.getEncoder().encode((user + ":" + password).getBytes()));
//		}
//		catch (IllegalArgumentException e)
//		{
//			// Not thrown
//		}
//
//		return "";
//	}
