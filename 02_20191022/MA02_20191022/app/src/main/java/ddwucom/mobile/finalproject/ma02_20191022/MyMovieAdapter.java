package ddwucom.mobile.finalproject.ma02_20191022;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Base64;

public class MyMovieAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<MovieDTO> list;
    private NaverNetworkManager networkManager = null;
    private ImageFileManager imageFileManager = null;

    public MyMovieAdapter(Context context, int resource, ArrayList<MovieDTO> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        networkManager = new NaverNetworkManager(context);
        imageFileManager = new ImageFileManager(context);
    }

    public void setList(ArrayList<MovieDTO> list) {
        this.list = list;
        notifyDataSetChanged();
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

        MovieDTO dto = list.get(position);

        viewHolder.tvTitle.setText(dto.getTitle());
        viewHolder.tvYear.setText(dto.getPubDate());
        viewHolder.tvActor.setText(dto.getActor());


        if(dto.getImgLink() == null) {
            viewHolder.ivPoster.setImageResource(R.mipmap.ic_launcher);
            return view;
        }

        Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(dto.getImgLink());

        if(savedBitmap != null) {
            viewHolder.ivPoster.setImageBitmap(savedBitmap);
        }
        else {
            viewHolder.ivPoster.setImageResource(R.mipmap.ic_launcher_movie);
            new GetImageAsyncTask(viewHolder).execute(dto.getImgLink());
        }

        return view;
    }

    static class ViewHolder {
        public ImageView ivPoster = null;
        public TextView tvTitle = null;
        public TextView tvYear = null;
        public TextView tvActor = null;
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        ViewHolder viewHolder;
        String imageAddress;

        public GetImageAsyncTask(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result = null;

            result = networkManager.downloadImage(imageAddress);

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                viewHolder.ivPoster.setImageBitmap(bitmap);
                imageFileManager.saveBitmapToTemporary(bitmap, imageAddress);
            }
        }
    }
}
