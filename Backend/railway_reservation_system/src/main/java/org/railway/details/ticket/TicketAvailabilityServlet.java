package org.railway.details.ticket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.railway.pojo.AvailableTicketPojo;
import org.railway.pojo.Station;
import org.railway.pojo.Train;

import com.google.gson.Gson;

/**
 * Servlet implementation class TicketAvailabilityServlet
 */
@WebServlet(description = "fetches available tickets", urlPatterns = { "/ticketAvailabilityServlet" })
public class TicketAvailabilityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/railway_reservation_system";

	static final String USER = "root";
	static final String PASS = "root";

	/**
	 * Default constructor.
	 */
	public TicketAvailabilityServlet() {
		super();
	}

	private Train getTrainDetails(Connection conn, Integer trainId) throws Exception {
		String trainDetailsSql = "SELECT * FROM train WHERE train_id=?";
		PreparedStatement pstmt = conn.prepareStatement(trainDetailsSql);
		pstmt.setInt(1, trainId);
		ResultSet rs = pstmt.executeQuery();
		Train trainDetails = new Train();
		while (rs.next()) {
			trainDetails.setTrainName(rs.getString("train_name"));
			trainDetails.setFkSourceStationId(getStationDetails(conn, rs.getInt("fk_source_station_id")));
			trainDetails.setFkDstinationStationId(getStationDetails(conn, rs.getInt("fk_destination_station_id")));
			trainDetails.setTrainId(trainId);
		}
		return trainDetails;
	}

	private Station getStationDetails(Connection conn, Integer stationId) throws Exception {
		String stationDetailsSQL = "SELECT * FROM station where station_id=?";
		PreparedStatement pstmt = conn.prepareStatement(stationDetailsSQL);
		pstmt.setInt(1, stationId);
		ResultSet rs = pstmt.executeQuery();
		Station stationDetail = new Station();
		while (rs.next()) {
			stationDetail.setStationId(stationId);
			stationDetail.setStationName(rs.getString("station_name"));
		}

		return stationDetail;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:5500");
	    response.setHeader("Access-Control-Allow-Methods", "GET");
		String uri = request.getScheme() + "://" + // "http" + "://
				request.getServerName() + // "myhost"
				":" + // ":"
				request.getServerPort() + // "8080"
				request.getRequestURI() + // "/people"
				"?" + // "?"
				request.getQueryString();
		System.out.println(uri);
		
		//parsing of input start
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(jb.toString());

		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(jb.toString());
			System.out.println(jsonObj);
		} catch (Exception e) {
			throw new IOException("Error parsing JSON request string");
		}
		
		Integer sourceStationId = Integer.valueOf(jsonObj.getInt("sourceStationId"));
		Integer destinationStationId = Integer.valueOf(jsonObj.getInt("destinationStationId"));
		String uiDate = jsonObj.getString("date");
		
		java.sql.Date parsedDate = null;
		try {
			java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(uiDate);
			parsedDate = new java.sql.Date(date.getTime());
		}catch(ParseException e) {
			e.printStackTrace();
		}
		
		//parsing of input end
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;
		PreparedStatement pstmt = null;

		List<AvailableTicketPojo> availableTicketList = new ArrayList<AvailableTicketPojo>();

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String availbaleTicketSql = "SELECT * FROM train_status INNER JOIN train ON train_status.`fk_train_id` = train.`train_id`"
					+ " WHERE train_status.`date` = ? AND train.`fk_source_station_id` = ? AND train.`fk_destination_station_id` = ?;";
			pstmt = conn.prepareStatement(availbaleTicketSql);
			pstmt.setDate(1, parsedDate);
			pstmt.setInt(2, sourceStationId);
			pstmt.setInt(3, destinationStationId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AvailableTicketPojo availableTicket = new AvailableTicketPojo();
				availableTicket.setFkTrainId(getTrainDetails(conn, rs.getInt("fk_train_id")));
				availableTicket.setAvailableSeat3A(rs.getInt("available_seat_3A"));
				availableTicket.setAvailableSeatSL(rs.getInt("available_seat_SL"));
				availableTicket.setDate(rs.getDate("date"));
				availableTicketList.add(availableTicket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		String data = gson.toJson(availableTicketList);
		System.out.println(data);
		out.write(data);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	
	
	
}
