package utcc.nontchaiyakarn.admissionnews;

import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private DataTABLE objDataTABLE;
    private UsageTABLE objUsageTABLE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connectedSQLite();  // Connected to SQLite

        //testAddValue(); // Test Add Value

        deleteAllData();

        syncJSONtoSQLite(); // Sync Data to SQLite


    }   // on Create

    private void deleteAllData() {

        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("AdmissionNews.db", MODE_APPEND, null);
        objSqLiteDatabase.delete("dataTABLE", null, null);
        objSqLiteDatabase.delete("usageTABLE", null, null);

    }   // deleteAllData

    private void syncJSONtoSQLite() {

        // SetupPolicy
        StrictMode.ThreadPolicy myPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myPolicy);

        // Sync ข้อมูลจาก Table

            // Constant
            InputStream objInputStream = null;  // โหลดไปใช้ไป
            String strJSON = null;  // จะเปลี่ยน Input Stream ให้เป็น String
            String strData = "http://nontc5.utcc-ict.com/AdmissionNews/JSONGetData.php";   // URL ของไฟล์ JSON ตาราง User
            HttpPost objHttpPost;   // ประกาศตัวแปรไว้

            // 1. Create InputStream
        try {

            HttpClient objHttpClient = new DefaultHttpClient();
            objHttpPost = new HttpPost(strData);

            HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
            HttpEntity objHttpEntity = objHttpResponse.getEntity();
            objInputStream = objHttpEntity.getContent();

        } catch (Exception e) {

            Log.d("AMNews", "InputStream ===>" + e.toString());

        }

            // 2.  Create strJSON เปลี่ยนสิ่งที่เรา Streaming มาให้เป็น String
        try {

            BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
            StringBuilder objStringBuilder = new StringBuilder();   // ตัวที่ทำหน้าที่รวม
            String strLine = null;  // ตัวแปรที่รับตัวที่ถูกตัดมา

            while ((strLine = objBufferedReader.readLine())!= null ) {  // ถ้า strLine ว่างเปล่า ก็ออกจาก Loop

                objStringBuilder.append(strLine);   // มีหน้าที่คอยผูก String ไปเรื่อย ๆ


            }   // While Loop
            objInputStream.close();                 // ถ้าหมด ก็ไม่ต้องโหลดต่อ
            strJSON = objStringBuilder.toString();


        } catch (Exception e) {

            Log.d("AMNews", "strJSON ==> "+e.toString());

        }

            // ข้้อที่ 3. Update SQLite     เอา strJSON ที่ได้มา มาใส่ใน SQLite
        try {

            final JSONArray objJsonArray = new JSONArray(strJSON);

            for (int i = 0; i < objJsonArray.length(); i++) {

                JSONObject object = objJsonArray.getJSONObject(i);  // เอา i มาแทนค่าตำแหน่งของ Array


                    // สำหรับ DataTABLE
                    String strSubject = object.getString("DataSubject");
                    String strIMG = object.getString("DataIMG");
                    String strDateStart = object.getString("DataDateStart");  // User เป็น Key ใน JSON
                    String strDateEnd = object.getString("DataDateEnd");
                    String strEvent = object.getString("DataEventType");
                    String strDescription = object.getString("DataDescription");  // User เป็น Key ใน JSON
                    String strLink = object.getString("DataLink");
                    String strInstitute = object.getString("DataInstitute");

                objDataTABLE.addNewData(strSubject, strIMG, strDateStart, strDateEnd, strEvent, strDescription, strLink, strInstitute);


            }   // วิ่งวนตามจำนวน แถวใน JSON

        } catch (Exception e) {

            Log.d("AMNews", "Update Error ==> "+e.toString());

        }


    }   // Sync data FROM JSON TO SQLite


    private void testAddValue() {

        objUsageTABLE.addNewUsage("1", "00000000012");

    }   //  Test Add Value


    private void connectedSQLite() {

        objDataTABLE = new DataTABLE(this);
        objUsageTABLE = new UsageTABLE(this);

    }   // connectedSQLite

}   // Main Class
