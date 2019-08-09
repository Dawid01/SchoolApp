package com.szczepaniak.dawid.appezn;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public class AlbumViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public AlbumViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {

    }
}