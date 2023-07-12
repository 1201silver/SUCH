package ddwucom.mobile.finalproject.ma02_20191022;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyPageAdapter extends BaseAdapter {

    public static final String TAG = "MyPageAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;

    private ArrayList<MovieDTO> list;
    private ImageFileManager imageFileManager = null;

    public MyPageAdapter(Context context, int resource, ArrayList<MovieDTO> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageFileManager = new ImageFileManager(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MovieDTO getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        Log.d(TAG, "MyPageAdapter / constructor: " +list.get(position));

        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.tvTitle = view.findViewById(R.id.tvTitleS);
            viewHolder.tvYear = view.findViewById(R.id.tvYearS);
            viewHolder.tvActor = view.findViewById(R.id.tvActorS);
            viewHolder.ivPoster = view.findViewById(R.id.ivPoster);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvTitle.setText(list.get(position).getTitle());
        viewHolder.tvYear.setText(list.get(position).getPubDate());
        viewHolder.tvActor.setText(list.get(position).getActor());

        Log.d(TAG, "MyPageAdapter / getView: " +list.get(position).getImgFileName());

        Bitmap bitmap = imageFileManager.getBitmapFromExternal(list.get(position).getImgFileName());

        if(bitmap != null) {
            viewHolder.ivPoster.setImageBitmap(bitmap);
        }
        else {
            viewHolder.ivPoster.setImageResource(R.mipmap.ic_launcher_movie);
        }

        return view;
    }

    static class ViewHolder {
        public ImageView ivPoster = null;
        public TextView tvTitle = null;
        public TextView tvYear = null;
        public TextView tvActor = null;
    }
}
