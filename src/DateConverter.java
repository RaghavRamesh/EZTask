// @author Rajalakshmi Ramachandran A0088634U

public class DateConverter {

	private int day, year, mon;
	private String month;

	public void convertUserEnteredDate(String userDate) {
		year = Integer.parseInt(userDate.substring(0, 4));
		System.out.println(year);
		mon = Integer.parseInt(userDate.substring(4, 6));
		System.out.println(mon);
		switch (mon) {
		case 1: {
			month = "Jan";
			break;
		}
		case 2: {
			month = "Feb";
			break;
		}
		case 3: {
			month = "Mar";
			break;
		}
		case 4: {
			month = "Apr";
			break;
		}
		case 5: {
			month = "May";
			break;
		}
		case 6: {
			month = "Jun";
			break;
		}
		case 7: {
			month = "Jul";
			break;
		}
		case 8: {
			month = "Aug";
			break;
		}
		case 9: {
			month = "Sep";
			break;
		}
		case 10: {
			month = "Oct";
			break;
		}
		case 11: {
			month = "Nov";
			break;
		}
		case 12: {
			month = "Dec";
			break;
		}
		default:
			month = "null";
		}
		System.out.println(month);
		day = Integer.parseInt(userDate.substring(6, 8));
		System.out.println(day);
	}

	public String toString() {
		return day + "-" + month + "-" + year;
	}

	public int getDate() {
		return day;
	}

	public int getMonth() {
		return mon;
	}

	public int getYear() {
		return year;
	}

}
