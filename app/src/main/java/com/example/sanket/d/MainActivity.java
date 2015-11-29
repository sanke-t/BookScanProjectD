package com.example.sanket.d;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    public String codeContent, codeFormat, img, dd;
    private Helper h;
    private RecyclerView r;
    private CustomAdapter ad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setResultDisplayDuration(0);
        integrator.setPrompt(String.valueOf("Scan a Bar Code"));
        integrator.setScanningRectangle(600, 400);
        integrator.setCameraId(0);
        h=new Helper(this,null,null,1);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void scanNow(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setResultDisplayDuration(0);
        integrator.setPrompt(String.valueOf("Scan a Bar Code"));
        integrator.setScanningRectangle(600, 400);
        integrator.setCameraId(0);
        integrator.initiateScan();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            codeFormat = scanningResult.getFormatName();
            codeContent = scanningResult.getContents();
            StringBuilder url1 = new StringBuilder();
            url1.append("https://www.googleapis.com/books/v1/volumes?q=isbn:" + codeContent);
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest fetch = new JsonObjectRequest(Request.Method.GET, url1.toString(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray items = response.getJSONArray("items");
                        for (int i=0;i<items.length();i++)
                        {
                            JSONObject random = items.getJSONObject(i);
                            JSONObject v = random.getJSONObject("volumeInfo");
                            String site = v.getString("title");
                            /* metaTextView = (TextView) findViewById(R.id.metaTextView);
                            metaTextView.setText(site); */
                            JSONArray a = v.getJSONArray("authors");
                            for(int j=0;j<a.length();j++) {
                                dd = a.getString(i);
                                /* metaTextVieww =(TextView) findViewById(R.id.metaTextVieww);
                                   metaTextVieww.setText("BY ----> " + dd); */
                            }
                            String p = v.getString("publisher");
                            /* metaTextVie = (TextView) findViewById(R.id.metaTextVie);
                            metaTextVie.setText("PUBLISHER ----> "+p);
                            String d = v.getString("description");
                            metaTextVi = (TextView) findViewById(R.id.metaTextVi);
                            metaTextVi.setText("ABOUT:"+d); */
                            h.addBook(site, dd, p);
                            final LinearLayoutManager lay = new LinearLayoutManager(getApplicationContext());
                            lay.setOrientation(LinearLayoutManager.VERTICAL);
                            r=(RecyclerView) findViewById(R.id.rv);
                            r.setLayoutManager(lay);
                            ad = new CustomAdapter(getApplicationContext(), h.getTable());
                            r.setAdapter(ad);
                            ad.notifyDataSetChanged();
                            /* JSONObject w =v.getJSONObject("imageLinks");
                               img=w.getString("thumbnail"); */                        }


                    /*JSONObject items = response.getJSONObject("items");
                    String site = items.getString("title");
                    TextView metaTextView = (TextView) findViewById(R.id.metaTextView);
                    metaTextView.setText(site);
                    JSONArray dd = items.getJSONArray("authors");
                    TextView metaTextVieww = (TextView) findViewById(R.id.metaTextVieww);
                    metaTextVieww.setText(dd.toString());*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(fetch);
             /* ImageRequest imgRequest = new ImageRequest(img,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            Log.d("SUCCESS_RESPONSE", "Image fetched!");
                            ImageView mImageView = (ImageView) findViewById(R.id.barcode_image_view);
                            mImageView.setImageBitmap(response);

                        }
                    }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("error again", "Url2 failed also !");
                    error.printStackTrace();
                }
            });
            queue.add(imgRequest); */
        }


    }


    }



