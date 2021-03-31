package threadExcutioner;

// ParBegin
//      Execute Bruno's threads wait for them to finish
// ParEnd
//      Execute new thread to treat data received from Bruno's threads
public class Main {

    public static void main(String args[]) throws Exception
    {
        //Instantiation of threads (objects)
        //This classes / objects are just an example. TODO: change for Bruno's Classes (threads)
        One o = new One(); //Example thread
        Two t = new Two(); //Example thread

        //Threads Starts (starts executing threads)
        o.start();
        t.start();

        //Threads Joins (join wait for thread to die)
        t.join();
        o.join();

        //Create and execute last Thread for merging into a final image
        mergeImg imageMerged = new mergeImg(array);
        imageMerged.start();
        imageMerged.join();

        System.out.println("All threads has been executed!");

        //code to pass final image to UI Controller
    }

    private static class mergeImg extends Thread {
        private static Type[] imgArray;

        public mergeImg(Type[] imgArray){
            this.imgArray = imgArray;
        }

        public void run(){

            //code to merge into a final image

            System.out.println("Im a test of mergeImg Thread!");
        }

    }
}
