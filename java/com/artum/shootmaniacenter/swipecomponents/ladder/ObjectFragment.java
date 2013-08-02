package com.artum.shootmaniacenter.swipecomponents.ladder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.artum.shootmaniacenter.R;
import com.artum.shootmaniacenter.ShowPlayer;
import com.artum.shootmaniacenter.adapters.ladderAdapter;
import com.artum.shootmaniacenter.structures.nadeo.RankElement;
import com.artum.shootmaniacenter.utilities.HtmlFormatter;
import com.artum.shootmaniacenter.utilities.NadeoDataSeeker;
import com.artum.shootmaniacenter.utilities.cache.LadderCacheManager;
import com.artum.shootmaniacenter.utilities.jsonDecrypter;

import java.util.ArrayList;

/**
 * Created by artum on 25/05/13.
 *
 * Frammento di Ladder (Contenente la ListView)
 *
 */

// Instances of this class are fragments representing a single
// object in our collection.
public class ObjectFragment extends Fragment {

    public static final String ARG_OBJECT = "object";
    ListView list;
    ListAdapter adapter;

    // Data
    final ArrayList<RankElement> rankList = new ArrayList<RankElement>();
    RankElement[] ladderStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        Bundle args = getArguments();
        list = (ListView)rootView.findViewById(R.id.ListLadder);
        adapter = new ladderAdapter(getActivity() , rankList);

        switch (args.getInt(ARG_OBJECT))
        {
            case 0:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new _getEliteLadder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                else
                    new _getEliteLadder().execute("");
                break;
            case 1:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new _getStormLadder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                else
                    new _getStormLadder().execute("");
                break;
            case 2:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    new _getJoustLadder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                else
                    new _getJoustLadder().execute("");
                break;
        }
        return rootView;
    }

    AdapterView.OnItemClickListener ladderListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if(ladderStatus != null)
            {
                RankElement selected = ladderStatus[i];
                Intent myIntent = new Intent(getActivity(), ShowPlayer.class);
                myIntent.putExtra("name", selected.playerData.login);
                getActivity().startActivity(myIntent);
            }
        }
    };

    private class _getEliteLadder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            rankList.clear();
            setLadderList("elite_ladder");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            list.setAdapter(adapter);
            list.setOnItemClickListener(ladderListener);
        }
    }

    private class _getStormLadder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            rankList.clear();
            setLadderList("storm_ladder");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            list.setAdapter(adapter);
            list.setOnItemClickListener(ladderListener);
        }
    }

    private class _getJoustLadder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            rankList.clear();
            setLadderList("joust_ladder");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            list.setAdapter(adapter);
            list.setOnItemClickListener(ladderListener);
        }
    }

    private void setLadderList(String title)
    {
        NadeoDataSeeker seeker = new NadeoDataSeeker();
        jsonDecrypter decrypter = new jsonDecrypter();
        String nadeoJsonString;

        if(LadderCacheManager.hasToUpdate(getActivity(), title))
        {
            if(title.equals("elite_ladder"))
                nadeoJsonString = seeker.getEliteLadder(0, 10);
            else if(title.equals("storm_ladder"))
                nadeoJsonString = seeker.getStormLadder(0, 10);
            else
                nadeoJsonString = seeker.getJoustLadder(0, 10);

            if(!nadeoJsonString.equals("Nope"))
            {
                LadderCacheManager.saveToCache(getActivity(), title, nadeoJsonString);
                ladderStatus = decrypter.getRanksElementFromSegment(nadeoJsonString, 10);
                for (RankElement temp : ladderStatus)
                    rankList.add(temp);
            }
            else if((nadeoJsonString = LadderCacheManager.loadFromCache(getActivity(), title)) != null)
            {
                ladderStatus = decrypter.getRanksElementFromSegment(nadeoJsonString, 10);
                for (RankElement temp : ladderStatus)
                    rankList.add(temp);
            }
            else
            {
                RankElement tempElement = new RankElement();
                tempElement.rank = -1;
                rankList.add(tempElement);
            }
        }
        else
        {

            if((nadeoJsonString = LadderCacheManager.loadFromCache(getActivity(), title)) != null)
            {
                ladderStatus = decrypter.getRanksElementFromSegment(nadeoJsonString, 10);
                for (RankElement temp : ladderStatus)
                    rankList.add(temp);
            }
            else
            {
                RankElement tempElement = new RankElement();
                tempElement.rank = -1;
                rankList.add(tempElement);
            }
        }
    }

}

