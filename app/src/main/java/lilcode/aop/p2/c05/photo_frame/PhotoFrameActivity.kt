package lilcode.aop.p2.c05.photo_frame

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

    private val photoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }

    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoframe)

        getPhotoUriFromIntent()

        startTimer()
    }

    private fun getPhotoUriFromIntent() {
        // 인텐트에 들어있는 데이터 가져오기
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        // 5초에 한 번씩 전환
        timer(period = 5 * 1000) {
            // timer는 메인 스레드가 아님
            runOnUiThread { // 메인 스레드
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1


                //  현재 이미지를 보여주고
                backgroundPhotoImageView.setImageURI(photoList[current])

                // 투명도 설정
                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoList[next])
                // 애니메이션으로 다음 이미지를 1초간 전환해 보여줌
                photoImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }


        }
    }
}