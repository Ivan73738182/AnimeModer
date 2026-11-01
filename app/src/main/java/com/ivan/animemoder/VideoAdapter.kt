package com.ivan.animemoder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView

class VideoAdapter(
    private val videos: List<Video>,
    private val onApprove: (Video) -> Unit,
    private val onReject: (Video) -> Unit
) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvDesc)
        val tvPhone: TextView = view.findViewById(R.id.tvPhone)
        val playerView: PlayerView = view.findViewById(R.id.playerView)
        val btnApprove: Button = view.findViewById(R.id.btnApprove)
        val btnReject: Button = view.findViewById(R.id.btnReject)
        var player: ExoPlayer? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]
        holder.tvTitle.text = video.title
        holder.tvDesc.text = video.description
        holder.tvPhone.text = video.author_phone

        holder.player?.release()
        val player = ExoPlayer.Builder(holder.itemView.context).build()
        holder.player = player
        holder.playerView.player = player
        
        val mediaItem = MediaItem.fromUri(video.video_url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = false

        holder.btnApprove.setOnClickListener { 
            holder.player?.stop()
            onApprove(video) 
        }
        holder.btnReject.setOnClickListener { 
            holder.player?.stop()
            onReject(video) 
        }
    }

    override fun getItemCount() = videos.size
}
