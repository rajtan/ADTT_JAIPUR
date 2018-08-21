package com.rto_driving_test_rajasthan.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rto_driving_test_rajasthan.Authorization.ActAppoimentList;
import com.rto_driving_test_rajasthan.Models.AppointmentModel;
import com.rto_driving_test_rajasthan.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by cnvg on 11/4/17.
 */

public class DateSelectAdapter extends RecyclerView.Adapter<DateSelectAdapter.MyViewHolder> {

    private List<AppointmentModel> appointmentModels;
    private ArrayList<AppointmentModel> arSearchItem;

    OnItemClickAdapter onItemClickAdapter;

    private Activity mcontext;
    int index;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName,tvId;
        ImageView status;
        RelativeLayout rlMain;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_date);
            tvId = (TextView) view.findViewById(R.id.tv_id);
            status = (ImageView) view.findViewById(R.id.iv_date);
            rlMain = (RelativeLayout) view.findViewById(R.id.rl_select_date);

        }
    }

    public DateSelectAdapter(ActAppoimentList context, List<AppointmentModel> appointmentModels_, OnItemClickAdapter onItemClickAdapter_) {
        this.appointmentModels = appointmentModels_;
        this.arSearchItem = new ArrayList<AppointmentModel>();
        this.arSearchItem.addAll(appointmentModels);
        this.mcontext = context;
        this.onItemClickAdapter=onItemClickAdapter_;

    }

    public void setList(List<AppointmentModel> appointmentModels_) {
        this.appointmentModels=appointmentModels_;
        this.notifyDataSetChanged();

    }

    @Override
    public DateSelectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_appoiment, parent, false);
        return new DateSelectAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final DateSelectAdapter.MyViewHolder holder, final int position) {
        index = position;
//            AppoimentBean stdData10 = stdData10s.get(position);

        /*holder.tvName.setText(stdData10s.get(position).getApName());
        holder.tvId.setText("Id: "+stdData10s.get(position).getApAppNo());*/


        holder.tvName.setText(appointmentModels.get(position).getApplicant_Name());
        holder.tvId.setText(appointmentModels.get(position).getReference_Number());
        holder.status.setBackgroundResource(R.drawable.pic);

      /*  if(appointmentModels.get(position).getAPPLICANT_PIC().equals("")||appointmentModels.get(position).getAPPLICANT_PIC()==null)
        {
//             tdData10s.get(position)
            holder.status.setBackgroundResource(R.drawable.pic);
        }
        else
        {
            Picasso.with(mcontext).load(appointmentModels.get(position).getAPPLICANT_PIC().toString()).into(holder.status);
        }*/


       /* holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=appointmentModels.get(position).getReceipt_Number();

//                    mcontext.startActivity(new Intent(mcontext, LoginActivity.class).putExtra("app_id",id));

                Intent in = new Intent(mcontext,ActRetestAppliDetails.class);
                in.putExtra("diff","fresh");
                mcontext.startActivity(in);
            }
        });*/

       holder.rlMain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
//               onItemClickAdapter.onclick(R.id.rl_select_date,null,position);

               onItemClickAdapter.click(R.id.rl_select_date,null,position);

           }
       });

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        appointmentModels.clear();
        if (charText.length() == 0) {
            appointmentModels.addAll(arSearchItem);
        } else {
            for (AppointmentModel wp : arSearchItem) {
                if (wp.getApplicant_Name().toLowerCase(Locale.getDefault()).contains(charText))
//                if (wp.getCatName().startsWith(charText))
                {
                    appointmentModels.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return appointmentModels.size();
    }
}