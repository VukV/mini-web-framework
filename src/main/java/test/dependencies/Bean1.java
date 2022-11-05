package test.dependencies;

import framework.annotations.Bean;
import framework.annotations.Scope;

@Bean(scope = Scope.PROTOTYPE)
public class Bean1 {

    public Bean1() {
    }
}
