package com.artum.shootmaniacenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.artum.shootmaniacenter.structures.nadeo.PlayerData;
import com.artum.shootmaniacenter.utilities.BufferBitmap;
import com.artum.shootmaniacenter.utilities.HtmlFormatter;
import com.artum.shootmaniacenter.utilities.NadeoDataSeeker;
import com.artum.shootmaniacenter.utilities.UnicodeInterpreter;
import com.artum.shootmaniacenter.utilities.jsonDecrypter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artum on 22/05/13.
 */
public class ShowPlayer extends Activity {

    private String playerToSee = "null";

    TextView playerNick;
    TextView playerLogin;
    TextView playerRegion;
    TextView elitePointsText;
    TextView stormPointsText;
    TextView joustPointsText;
    TextView eliteRankText;
    TextView stormRankText;
    TextView joustRankText;
    ImageView zoneImg;
    Handler mHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_info);
        Intent intent = getIntent();
        mHandler = new Handler();

        playerToSee = intent.getStringExtra("name");
        playerNick = (TextView)findViewById(R.id.playerNick);
        playerLogin = (TextView)findViewById(R.id.playerLogin);
        playerRegion = (TextView)findViewById(R.id.playerRegion);

        elitePointsText = (TextView)findViewById(R.id.elitepoints);
        eliteRankText = (TextView)findViewById(R.id.eliterank);
        stormPointsText = (TextView)findViewById(R.id.stormpoints);
        stormRankText = (TextView)findViewById(R.id.stormrank);
        joustPointsText = (TextView)findViewById(R.id.joustpoints);
        joustRankText = (TextView)findViewById(R.id.joustrank);

        zoneImg = (ImageView)findViewById(R.id.regionFlag);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        _getInfos();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                    finish();
                break;

        }

        return true;
    }

    private void _getInfos()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NadeoDataSeeker seeker = new NadeoDataSeeker();
                jsonDecrypter decrypter = new jsonDecrypter();
                final HtmlFormatter formatter = new HtmlFormatter();
                final UnicodeInterpreter unicodeInterpreter = new UnicodeInterpreter();

                String playerSegment = seeker.getPlayerInfo(playerToSee);
                String playerEliteRank = seeker.getPlayerRank("elite", playerToSee);
                String playerStormRank = seeker.getPlayerRank("storm", playerToSee);
                String playerJoustRank = seeker.getPlayerRank("joust", playerToSee);

                Pattern pattern = Pattern.compile("^\\{\"message\":\"+(.+)\"\\}$");
                final Matcher matcher = pattern.matcher(playerSegment);

                if(matcher.find())
                {

                    mHandler.post(new Runnable() {
                        public void run() {
                            AlertDialog alertDialog;
                            alertDialog = new AlertDialog.Builder(ShowPlayer.this).create();
                            alertDialog.setTitle("Error");
                            alertDialog.setMessage(matcher.group(1));
                            alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    finish();
                                }
                            });
                            alertDialog.show();
                        }
                    });
                }
                else if (playerSegment != "Nope")
                {

                    PlayerData temp = null;


                    try {
                        temp = decrypter.getPlayerFromSegment(new JSONObject(playerSegment));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {

                        JSONObject tempObject = new JSONObject(playerEliteRank);
                        final double EliteLP = tempObject.getDouble("points");
                        JSONArray array = tempObject.getJSONArray("ranks");
                        final int ElitePosition = array.getJSONObject(0).getInt("rank");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                elitePointsText.setText(EliteLP + "LP");
                                eliteRankText.setText("#" + ElitePosition);
                            }
                        });
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                elitePointsText.setText("0 LP");
                                eliteRankText.setText("NOT RANKED");
                            }
                        });
                    }

                    try {
                    JSONObject tempObject = new JSONObject(playerStormRank);
                        final double StormLP = tempObject.getDouble("points");
                    JSONArray array = tempObject.getJSONArray("ranks");
                        final int StormPosition = array.getJSONObject(0).getInt("rank");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stormPointsText.setText(StormLP + "LP");
                                stormRankText.setText("#" + StormPosition);
                            }
                        });
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stormPointsText.setText("0 LP");
                                stormRankText.setText("NOT RANKED");
                            }
                        });
                    }

                    try {
                        JSONObject tempObject = new JSONObject(playerJoustRank);
                        final double JoustLP = tempObject.getDouble("points");
                        JSONArray array = tempObject.getJSONArray("ranks");
                        final int JoustPosition = array.getJSONObject(0).getInt("rank");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                joustPointsText.setText(JoustLP + "LP");
                                joustRankText.setText("#" + JoustPosition);
                            }
                             });
                    } catch (JSONException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                joustPointsText.setText("0 LP");
                                joustRankText.setText("NOT RANKED");
                            }
                        });
                    }

                    final PlayerData data = temp;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playerNick.setText(Html.fromHtml(formatter.fromStringToHtml(data.nick)));
                                playerLogin.setText(data.login);
                                playerRegion.setText(unicodeInterpreter.resolveString(data.region));


                                playerLogin.setSelected(true);
                                playerRegion.setSelected(true);
                                playerNick.setSelected(true);
                                elitePointsText.setSelected(true);
                                eliteRankText.setSelected(true);
                                stormRankText.setSelected(true);
                                stormPointsText.setSelected(true);
                                joustPointsText.setSelected(true);
                                joustRankText.setSelected(true);

                            }
                        });


                        String imgURL = seeker.zoneJpgURL(data.regionId);
                        final Bitmap imgBuffer = BufferBitmap.loadBitmap(imgURL);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(imgBuffer != null)
                                    zoneImg.setImageBitmap(imgBuffer);
                            }
                        });

                }
                else
                {
                    final PlayerData data = new PlayerData();
                    data.nick = "Error retriving data from Nadeo...";
                    data.login = "Error";
                    data.region = "";

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playerNick.setText(Html.fromHtml(formatter.fromStringToHtml(data.nick)));
                            playerLogin.setText(data.login);
                            playerRegion.setText(unicodeInterpreter.resolveString(data.region));

                            playerLogin.setSelected(true);
                            playerRegion.setSelected(true);
                            playerNick.setSelected(true);
                        }
                    });
                }
            }
        }).start();
    }
}