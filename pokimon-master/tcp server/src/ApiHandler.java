import org.json.JSONArray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * This class handles Matrix-related tasks
 */
public class ApiHandler implements IHandler {
    private String poke1;
    private String poke2;
    private volatile boolean doWork = true;
    private static HttpURLConnection connection;

    @Override
    public void resetMembers() {
        this.poke1 = null;
        this.poke2 = null;
        this.doWork = true;
    }

    @Override
    public void handle(InputStream fromClient, OutputStream toClient) throws IOException, ClassNotFoundException {

        ObjectInputStream objectInputStream = new ObjectInputStream(fromClient);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(toClient);

        boolean doWork = true;

        while (doWork) {
            poke1 = (objectInputStream.readObject().toString());

            if (poke1 == "stop") {
                doWork = false;
                break;
            }

            poke2 = (objectInputStream.readObject().toString());
            String pokemon1 = getData(poke1);
            String pokemon2 = getData(poke2);
            int power1 = parse(pokemon1);
            int power2 = parse(pokemon2);

            if (power1 > power2)
                objectOutputStream.writeObject(poke1);
            else
                objectOutputStream.writeObject(poke2);


//Get Response
            InputStream is = connection.getInputStream();
            System.out.println(connection.getContentType());


        }
        }
        public String getData(String poke) throws IOException {
            BufferedReader reader;
            String line;
            StringBuffer responseContent = new StringBuffer();
            URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + poke);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            int status = connection.getResponseCode();
            System.out.println(status);
            if (status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            }
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
            connection.disconnect();
            System.out.println(responseContent.toString());
            return responseContent.toString();
        }
        public static int parse(String responseBody){
            JSONArray data = new JSONArray(responseBody);
            int power= data.getInt(Integer.parseInt("base_experience"));

            return power;
        }
    }



