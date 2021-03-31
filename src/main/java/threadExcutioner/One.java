package threadExcutioner;

public class One extends Thread{

    public void run()
    {

        try
        {
            Two t = new Two();
            t.join();

            System.out.println("I Belong to THREAD One ");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
