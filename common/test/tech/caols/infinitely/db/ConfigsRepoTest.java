package tech.caols.infinitely.db;

import tech.caols.infinitely.consts.ConfigsKeys;
import tech.caols.infinitely.datamodels.Configs;
import tech.caols.infinitely.repositories.ConfigsRepository;

import java.text.DateFormat;
import java.util.Date;

public class ConfigsRepoTest {

    public static void main(String[] args) {
        ConfigsRepository configsRepository = new ConfigsRepository();
        configsRepository.findAll().forEach(System.out::println);

        Configs configs = configsRepository.findByKey(ConfigsKeys.LastUpdateLeveledResource);
        System.out.println(configs.getValue());
//        System.out.println(DateFormat.getInstance());

//        Configs configs = new Configs();
//        configs.setKey(ConfigsKeys.LastUpdateLeveledResource);
//        configs.setValue(DateFormat.getInstance().format(new Date()));
//        System.out.println(configsRepository.save(configs));
    }

}
