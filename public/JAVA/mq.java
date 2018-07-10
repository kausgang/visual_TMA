import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFException;
//import com.ibm.mq.headers.pcf.PCFMessageAgent;
import com.ibm.mq.jmqi.JmqiObject;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import com.ibm.mq.MQException;

public class mq {

    private String queuemanager,queue;

    public mq(String hostname, String port, String userID, String password, String channel,String queuemanager,String queue) {

        //SET MQ ENVIRONMENT VARIABLE TO CONNECT TO QUEUE MANAGER
        MQEnvironment.hostname = hostname;
        MQEnvironment.port = Integer.parseInt(port);
        MQEnvironment.userID = userID;
        MQEnvironment.password = password;
        MQEnvironment.channel = channel;

        this.queuemanager = queuemanager;
        this.queue = queue;
    }



//    KAUSTAV
    public int check_depth(){

        try{
            MQQueueManager qm = new MQQueueManager(queuemanager);

            //QUEUEMANAGER OPEN OPTION FOR INPUT(GET MESSAGE) & INQUIRE(TO CHECK DEPTH)
            int openOptionArg = CMQC.MQOO_INPUT_AS_Q_DEF|CMQC.MQOO_INQUIRE; //INQUIRE for checking depth
            MQQueue q = qm.accessQueue(queue,openOptionArg);

            int depth = q.getCurrentDepth();

            q.close();
            qm.disconnect();

            return depth;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

//    PREETAM
    public MQMessage message_content() {

        try {
            MQQueueManager qm = new MQQueueManager(queuemanager);
           //QUEUEMANAGER OPEN OPTION FOR BROWSE MESSAGE IN QUEUE
            int openOptionArg = CMQC.MQOO_FAIL_IF_QUIESCING | CMQC.MQOO_INPUT_SHARED | CMQC.MQOO_BROWSE;
            MQQueue q = qm.accessQueue(queue,openOptionArg);

            MQMessage msg = new MQMessage();
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            gmo.options = CMQC.MQGMO_WAIT | CMQC.MQGMO_BROWSE_FIRST;
            gmo.matchOptions = CMQC.MQMO_NONE;
//            gmo.waitInterval=6000;
            gmo.waitInterval=1000;

            boolean messagepresent=true;
            while(messagepresent) {
                try {
                    // read message
                    q.get(msg,gmo);
                    // Print text
                    String msgText = msg.readStringOfByteLength(msg.getMessageLength());
                    System.out.println("msg text :" + msgText);

                    gmo.options = CMQC.MQGMO_WAIT | CMQC.MQGMO_BROWSE_NEXT;
                } catch (MQException e) {

                    if (e.reasonCode == e.MQRC_NO_MSG_AVAILABLE) { //THIS CONSTANT IS NOT AVAILABLE ANY MORE
                        System.out.println("no messages are available");
                    }
                    messagepresent = false;
                }

            }q.close();
            qm.disconnect();
            return msg;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    KAUSTAV
    public MQMessage get_message(){

        try{

            MQQueueManager qm = new MQQueueManager(queuemanager);

            //QUEUEMANAGER OPEN OPTION FOR INPUT(GET MESSAGE) & INQUIRE(TO CHECK DEPTH)
            int openOptionArg = CMQC.MQOO_INPUT_AS_Q_DEF|CMQC.MQOO_INQUIRE; //INQUIR    E for checking depth
            MQQueue q = qm.accessQueue(queue,openOptionArg);

            int depth = q.getCurrentDepth();

            if (depth > 0){

                MQMessage msg = new MQMessage();
                MQGetMessageOptions gmo = new MQGetMessageOptions();

                //MSG IS UPDATED WITH THE MSG RETRIEVED FROM QUEUE
                q.get(msg,gmo);

                q.close();
                qm.disconnect();

                return msg;
            }
            else{
                System.out.println("queue is empty");
                System.exit(0);
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

//    KAUSTAV
//    UPDATE
    public void put_message_data(String data){

        try{

            MQQueueManager qm = new MQQueueManager(queuemanager);
            int openOptionArg = CMQC.MQOO_BROWSE|CMQC.MQOO_INPUT_AS_Q_DEF|CMQC.MQOO_OUTPUT|CMQC.MQOO_INQUIRE; //THESE MANY OPEN OPTION IS PROBABLY NOT NEEDED
            MQQueue q = qm.accessQueue(queue,openOptionArg);

            MQMessage msg = new MQMessage();
            msg.writeString(data); //ADD CONTENT TO MSG-DATA
            q.put(msg); //PUT MSG IN QUEUE

            q.close();
            qm.disconnect();


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }




}