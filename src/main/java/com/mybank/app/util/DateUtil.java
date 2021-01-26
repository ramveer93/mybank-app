package com.mybank.app.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DateUtil {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public JSONObject getHumanReadableDateStrFromDbDate(JSONObject response) {
		this.LOGGER.info("Converting db dates to human readable dates");
		try {
			convertToHumanReadableDate(response, "deleted");
			convertToHumanReadableDate(response, "createdOn");
			convertToHumanReadableDate(response, "updatedOn");
			this.LOGGER.info("Succcessfully Converted DB dates to human readable format for response");
			return response;
		} catch (JSONException ex) {
			this.LOGGER.error("Error {} converting DB dates to human readable format for response ", ex.getMessage());
			throw new BankException(ex.getMessage());
		}

	}

	private void convertToHumanReadableDate(JSONObject dateObj, String key) {
		JSONObject dateStrObj = dateObj.getJSONObject(key);
		if (dateStrObj != null) {
			String newDateStr = dateStrObj.getString("dayOfMonth") + dateStrObj.getString("monthValue")
					+ dateStrObj.getString("year") + dateStrObj.getString("minute") + dateStrObj.getString("second");
			dateObj.put(key, newDateStr);
		} else {
			this.LOGGER.error("DB date is found as null so no conversion will happen ");
		}

	}
}
