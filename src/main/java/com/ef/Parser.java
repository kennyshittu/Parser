package com.ef;

import com.ef.commands.ParserCommand;
import com.ef.configs.ParserConfiguration;
import com.ef.dao.BlockedIPAddressDao;
import com.ef.dao.BlockedIPAddressDaoImpl;
import com.ef.dao.LogRowDao;
import com.ef.dao.LogRowDaoImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Parser  extends Application<ParserConfiguration> {
    public static void main(String[] args) throws Exception {
        new Parser().run(args);
    }

    @Override
    public void run(ParserConfiguration configuration, Environment environment) throws Exception {
    }

    @Override
    public void initialize(Bootstrap<ParserConfiguration> bootstrap) {
        Injector injector = createInjector();
        bootstrap.addCommand(injector.getInstance(ParserCommand.class));
    }

    private Injector createInjector() {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(LogRowDao.class).to(LogRowDaoImpl.class);
                bind(BlockedIPAddressDao.class).to(BlockedIPAddressDaoImpl.class);
            }
        });
    }
}
