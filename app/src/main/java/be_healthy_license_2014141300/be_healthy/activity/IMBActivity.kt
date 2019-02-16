package be_healthy_license_2014141300.be_healthy.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import be_healthy_license_2014141300.be_healthy.dialog.IMBDialog
import com.be_healthy_license_2014141300.be_healthy.R
import mehdi.sakout.fancybuttons.FancyButton

class IMBActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private var minArray=arrayOf<String>()
    private var maxArray=arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.n_activity_imb)
        val toolBar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.menu.setGroupCheckable(0, false, true)
        (findViewById<View>(R.id.back_button)).setOnClickListener { this.onBackPressed() }

        val ageText=findViewById<AppCompatEditText>(R.id.editText3)
        val weightText=findViewById<AppCompatEditText>(R.id.editText4)
        val heightText=findViewById<AppCompatEditText>(R.id.editText5)
        val button=findViewById<FancyButton>(R.id.start)
        button.setOnClickListener {
            if (!ageText.text.isEmpty() && !weightText.text.isEmpty() && !heightText.text.isEmpty()){
                val age=ageText.text.toString().toInt()
                val weight=weightText.text.toString().toInt()
                val height=heightText.text.toString().toInt()
                val result=calculateIMB(weight, height)
                val index=findIndex(age, result)
                showDialog(result, index)
            }
            else{
                Toast.makeText(this, resources.getString(R.string.empty_fields_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun findIndex(age:Int, result:Float):Int{
        var index=0
        if (age<=25){
            minArray=resources.getStringArray(R.array.young_min_range)
            maxArray=resources.getStringArray(R.array.young_max_range)
        }
        else{
            minArray=resources.getStringArray(R.array.old_min_range)
            maxArray=resources.getStringArray(R.array.old_max_range)
        }
        for(i in 0 until minArray.size){
            if (result>minArray[i].toFloat() && result<=maxArray[i].toFloat()){
                index=i
            }
        }
        return index
    }

    private fun calculateIMB(weight:Int, height:Int):Float{
        val result=weight.toFloat()/(height.toFloat()*height.toFloat())*100*100
        return (Math.round(result * 10.0) / 10.0).toFloat()
    }

    private fun showDialog(result:Float, index:Int){
        val dialog = IMBDialog()
        dialog.setData("Индекс массы тела: $result",
                (resources.getStringArray(R.array.fatty_diagnosis)[index]),
                resources.getStringArray(R.array.fat_advice)[index])
        dialog.show(fragmentManager, "")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main -> {
                val intent= Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            R.id.search -> {
                val intent= Intent(this, SearchActivity::class.java)
                startActivity(intent)
            }
            else->{
                Toast.makeText(this, resources.getString(R.string.error_info), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}
