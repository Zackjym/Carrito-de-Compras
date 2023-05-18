package com.example.shopify

// MainActivity.kt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.content.Context
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private lateinit var itemList: ArrayList<Item>
    private lateinit var adapter: CartAdapter
    private lateinit var listView: ListView
    private lateinit var nameEditText: EditText
    private lateinit var quantityEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var addButton: Button
    private lateinit var totalTextView: TextView
    private var total: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemList = ArrayList()
        adapter = CartAdapter(this, itemList)
        listView = findViewById(R.id.listView)
        listView.adapter = adapter

        nameEditText = findViewById(R.id.nameEditText)
        quantityEditText = findViewById(R.id.quantityEditText)
        priceEditText = findViewById(R.id.priceEditText)
        addButton = findViewById(R.id.addButton)
        totalTextView = findViewById(R.id.totalTextView)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val quantity = quantityEditText.text.toString().toIntOrNull()
            val price = priceEditText.text.toString().toDoubleOrNull()

            if (name.isNotEmpty() && quantity != null && price != null) {
                val item = Item(name, quantity, price)
                itemList.add(item)
                adapter.notifyDataSetChanged()

                // Calcular el total
                total += quantity * price
                totalTextView.text = "Total: $${String.format("%.2f", total)}"

                // Restablecer los campos de entrada
                nameEditText.text.clear()
                quantityEditText.text.clear()
                priceEditText.text.clear()
            } else {
                Toast.makeText(this, "Ingrese valores válidos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

data class Item(val name: String, val quantity: Int, val price: Double)

class CartAdapter(context: Context, private val itemList: ArrayList<Item>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(position: Int): Any {
        return itemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item, parent, false)
            holder = ViewHolder()
            holder.nameTextView = view.findViewById(R.id.nameTextView)
            holder.quantityTextView = view.findViewById(R.id.quantityTextView)
            holder.priceTextView = view.findViewById(R.id.priceTextView)
            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val item = itemList[position]
        holder.nameTextView.text = item.name
        holder.quantityTextView.text = "Cantidad: ${item.quantity}"
        holder.priceTextView.text = "Precio: $${String.format("%.2f", item.price)}"

        return view
    }

    private class ViewHolder {
        lateinit var nameTextView: TextView
        lateinit var quantityTextView: TextView
        lateinit var priceTextView: TextView
    }
}
