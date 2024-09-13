package com.example.seminarapp

import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.example.seminarapp.databinding.ActivityMediaPickerBinding

class MediaPickerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMediaPickerBinding

    lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvPhotoPicker.setOnClickListener { photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }

        binding.tvVideoPicker.setOnClickListener { videoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)) }

    }

    private val photoPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        Glide.with(this).load(uri).into(binding.ivImageView)

    }

    private val videoPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        Glide.with(this).load(uri).into(binding.ivImageView)

        setPlayer(uri)


    }

    private fun setPlayer(uri: Uri?) {

        try {
            exoPlayer.release()
            binding.playerView.player?.release()
        }
        catch (e:Exception){

            }
            exoPlayer = ExoPlayer.Builder(this).build()

            val mediaItem = MediaItem.Builder().setUri(uri).setMimeType(MimeTypes.VIDEO_MP4).build()

            exoPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
                play()
            }.also { player ->
                binding.playerView.player = player
            }
            exoPlayer.addListener(playerListener)
    }

    private val playerListener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when(playbackState){
                Player.STATE_BUFFERING -> {
                    Unit
                }

                Player.STATE_ENDED -> {
                    Unit
                }

                Player.STATE_IDLE -> {
                    Unit
                }

                Player.STATE_READY -> {
                    Unit
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            exoPlayer?.release()
        }catch (e:Exception){}
    }

    override fun onPause() {
        super.onPause()
        try {
            exoPlayer?.pause()
        }
        catch (e:Exception){}
    }
}