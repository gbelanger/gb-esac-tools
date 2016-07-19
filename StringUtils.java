package gb.esac.tools;



public final class StringUtils {


    public static boolean searchString(String stringToFind, String stringToSearch) {
	
	boolean matchFound = false;
	if ( stringToSearch.equals(stringToFind) ) return true;
	else if ( stringToSearch.length() < stringToFind.length() ) return false;
	else {
	    int maxNumOfTests = stringToSearch.length() - stringToFind.length();
	    String substring = null;
	    int i = 0;
	    while ( i < maxNumOfTests && matchFound == false ) {
		substring = stringToSearch.substring(0+i, stringToFind.length()+i);
		if ( substring.equals(stringToFind) ) matchFound = true;
		else i++;
	    }
	}
	return matchFound;
    }

    public static boolean searchString(String stringToFind, String[] stringArrayToSearch) {
	
	int nMembers = stringArrayToSearch.length;
	String stringToSearch = null;
	boolean matchFound = false;
	int n=0;

	while ( n < nMembers && matchFound == false ) {

	    try {
		stringToSearch = stringArrayToSearch[n];
	    }
	    catch (ArrayIndexOutOfBoundsException e) {}

	    if ( stringToSearch.equals(stringToFind) ) 
		matchFound = true;
	    else if ( stringToSearch.length() < stringToFind.length() ) 
		matchFound = false;
	    else {
		int maxNumOfTests = stringToSearch.length() - stringToFind.length();
		String substring = null;
		int i = 0;
		while ( i < maxNumOfTests && matchFound == false ) {
		    substring = stringToSearch.substring(0+i, stringToFind.length()+i);
		    if ( substring.equals(stringToFind) ) 
			matchFound = true;
		    else 
			i++;
		}
	    }
	    n++;

	}
	return matchFound;
    }

}