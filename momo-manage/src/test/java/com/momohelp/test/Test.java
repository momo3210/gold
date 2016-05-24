package com.momohelp.test;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// SmSWebService service = new SmSWebService();
		// SmSWebServiceSoap serviceSoap = service.getSmSWebServiceSoap();
		// WsSendResponse response = serviceSoap.sendSms("154", "MOMO668",
		// "445566", "18530053050", " Է û У ", "", "");
		//
		// System.err.println(response.getReturnStatus());

		int i = (int) ((Math.random() * 5 + 1) * 1000);
		String id = String.valueOf(i);
		if (4 < id.length()) {
			id = id.substring(0, 4);
		}
		System.out.println(id);

	}

}
