package jp.ac.meijou.android.s241205182;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URLEncoder;

import jp.ac.meijou.android.s241205182.databinding.ActivityMain8Binding;  // ← Binding クラスをインポート

public class MainActivity8 extends AppCompatActivity {

    private ActivityMain8Binding binding;  // ViewBinding のフィールド
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain8Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ボタンクリックリスナー
        binding.buttonSearch.setOnClickListener(v -> {
            String postal1 = binding.editTextPostal1.getText().toString();
            String postal2 = binding.editTextPostal2.getText().toString();

            if (postal1.length() == 3 && postal2.length() == 4) {
                String fullPostalCode = postal1 + postal2;
                fetchAndParseLatLng(fullPostalCode);
            } else {
                binding.textViewResult.setText("郵便番号は3桁と4桁で入力してください");
            }
        });
    }

    private void fetchAndParseLatLng(String postalCode) {
        try {
            String encodedPostalCode = URLEncoder.encode(postalCode, "UTF-8");
            String url = "https://geoapi.heartrails.com/api/xml?method=searchByPostal&postal=" + encodedPostalCode;

            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> binding.textViewResult.setText(e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String xmlString = response.body().string();
                        try {
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            factory.setNamespaceAware(true);
                            XmlPullParser parser = factory.newPullParser();
                            parser.setInput(new java.io.StringReader(xmlString));

                            String x = null;
                            String y = null;

                            int eventType = parser.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                if (eventType == XmlPullParser.START_TAG) {
                                    String tagName = parser.getName();
                                    if ("x".equals(tagName)) {
                                        x = parser.nextText();
                                    } else if ("y".equals(tagName)) {
                                        y = parser.nextText();
                                    }
                                }
                                eventType = parser.next();
                            }

                            String result = "緯度(y): " + y + "\n経度(x): " + x;

                            runOnUiThread(() -> binding.textViewResult.setText(result));

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(() -> binding.textViewResult.setText("XML解析エラー"));
                        }
                    } else {
                        runOnUiThread(() -> binding.textViewResult.setText("APIエラー: " + response.code()));
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> binding.textViewResult.setText("エラーが発生しました"));
        }
    }
}
