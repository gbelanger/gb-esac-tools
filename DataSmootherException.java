package gb.esac.tools;


public class DataSmootherException extends Exception {

    public DataSmootherException () {
        super();
    }

    public DataSmootherException (String msg) {
        super(msg);
    }

    public DataSmootherException (String msg, Exception e) {
        super(msg+"\n", e);
    }
}

