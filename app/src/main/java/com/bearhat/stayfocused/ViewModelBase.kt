package  com.bearhat.stayfocused
interface ViewModelBase<T> {
    fun takeView(view:T)
    fun dropView()
}