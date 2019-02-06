package be_healthy_license_2014141300.be_healthy.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import be_healthy_license_2014141300.be_healthy.database.XLSReader
import com.be_healthy_license_2014141300.be_healthy.R
import io.reactivex.disposables.Disposable

class StartActivity : AppCompatActivity() {

    private var disposable:Disposable?=null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val reader = XLSReader.getInstance()
        disposable = reader.read(this).subscribe(
                {Log.e("LOG", "next")},
                {Log.e("LOG", "error")},
                {
                    val intent = Intent(this@StartActivity, MenuActivity::class.java)
                    startActivity(intent)
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (disposable!=null && !disposable!!.isDisposed){
            disposable?.dispose()
        }
    }
}
