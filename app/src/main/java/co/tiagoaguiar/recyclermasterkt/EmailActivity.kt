package co.tiagoaguiar.recyclermasterkt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.tiagoaguiar.recyclermasterkt.model.email
import co.tiagoaguiar.recyclermasterkt.model.fakeEmails
import co.tiagoaguiar.recyclermasterkt.view.EmailActivityUI
import com.mooveit.library.Fakeit
import org.jetbrains.anko.setContentView
import java.text.SimpleDateFormat
import java.util.*

class EmailActivity : AppCompatActivity() {

    private lateinit var adapter: EmailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fakeit.init()

        val emails = fakeEmails()
        adapter = EmailAdapter(emails)
        EmailActivityUI(adapter).setContentView(this)
    }

    fun addEmail() {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).parse(
            Fakeit.dateTime().dateFormatter()
        )

        adapter.emails.add(0,
            email {
                stared = false
                unread = true
                user = Fakeit.name().firstName()
                subject = Fakeit.company().name()
                date = SimpleDateFormat("d MMM", Locale("pt", "BR")).format(sdf)
                preview = mutableListOf<String>().apply {
                    repeat(10) {
                        add(Fakeit.lorem().words())
                    }
                }.joinToString(" ")
            })

        adapter.notifyItemInserted(0)
    }

}
