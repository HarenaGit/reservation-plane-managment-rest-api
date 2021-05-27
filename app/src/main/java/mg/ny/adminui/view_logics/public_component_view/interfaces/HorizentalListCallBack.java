package mg.ny.adminui.view_logics.public_component_view.interfaces;

@FunctionalInterface
public interface HorizentalListCallBack<T, U, V, R>{
    public R apply(T t, U u, V v);
}
