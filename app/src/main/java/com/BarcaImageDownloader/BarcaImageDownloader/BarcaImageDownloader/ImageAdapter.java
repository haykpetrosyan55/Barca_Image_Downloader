package com.BarcaImageDownloader.BarcaImageDownloader.BarcaImageDownloader;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final String KEY_FRAG = "tag";
    private Context context;
    private List<ImageModel> imgUrlList;
    private TextView urlView;

    public ImageAdapter(Context context, List<ImageModel> imgUrlList, TextView urlView) {
        this.context = context;
        this.imgUrlList = imgUrlList;
        this.urlView = urlView;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        final View view = inflater.inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
        final ImageModel model = imgUrlList.get(position);
        holder.image.setText(model.getImgNum());
        final FragmentManager fragmentTransaction = ((MainActivity) context).getFragmentManager();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!model.isDownload()) {
                    urlView.setText(model.getImgUrl());
                    ImageProvider.position = holder.getAdapterPosition();

                } else {
                    urlView.setText(model.getImgNum());
                    DialogFragment dialogFragment = new ImageDialogFragment();
                    dialogFragment.show(fragmentTransaction, KEY_FRAG);
                    ImageProvider.position = holder.getAdapterPosition();
                    holder.relativeLayout.setBackgroundColor(R.drawable.card_gradient);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgUrlList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private TextView image;
        private RelativeLayout relativeLayout;

        ImageViewHolder(final View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_num_id);
            relativeLayout = itemView.findViewById(R.id.for_card_back);
        }
    }
}
