package be_healthy_license_2014141300.be_healthy.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import be_healthy_license_2014141300.be_healthy.database.XLSReader
import com.be_healthy_license_2014141300.be_healthy.R
import com.be_healthy_license_2014141300.be_healthy.activity.MainActivity
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class StartActivity : AppCompatActivity() {

    var observer: Observer<Int> = object : Observer<Int> {
        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(value: Int?) {
        }

        override fun onError(e: Throwable) {
        }

        override fun onComplete() {
            var intent=Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        var reader = XLSReader.getInstance()
        reader.register(observer)
        reader.read(this)
    }
}
