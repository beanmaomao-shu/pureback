package com.zhouq.common.configuration;

import com.zhouq.entity.DB.Ecosystem;
import com.zhouq.entity.DB.Observation;
import com.zhouq.entity.DB.ObservationSpecies;
import com.zhouq.entity.DB.Species;
import com.zhouq.service.IEcosystemService;
import com.zhouq.service.IObservationService;
import com.zhouq.service.IObservationSpeciesService;
import com.zhouq.service.ISpeciesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MarineBiodivDataSeeder implements CommandLineRunner {

    private static final long ECOSYSTEM_START_ID = 1001L;
    private static final long SPECIES_START_ID = 2001L;
    private static final long MAP_SPECIES_START_ID = 2901L;
    private static final long OBSERVATION_START_ID = 3001L;
    private static final long OBSERVATION_SPECIES_START_ID = 4001L;
    private static final int SEED_SIZE = 60;

    private final IEcosystemService ecosystemService;
    private final ISpeciesService speciesService;
    private final IObservationService observationService;
    private final IObservationSpeciesService observationSpeciesService;

    public MarineBiodivDataSeeder(
            IEcosystemService ecosystemService,
            ISpeciesService speciesService,
            IObservationService observationService,
            IObservationSpeciesService observationSpeciesService
    ) {
        this.ecosystemService = ecosystemService;
        this.speciesService = speciesService;
        this.observationService = observationService;
        this.observationSpeciesService = observationSpeciesService;
    }

    @Override
    public void run(String... args) {
        seedEcosystems();
        seedSpecies();
        ensureVisibleSpeciesPoints();
        seedObservations();
        seedObservationSpecies();
    }

    private void seedEcosystems() {
        String[] types = {"红树林", "珊瑚礁", "海草床", "河口湿地", "近岸海域", "海湾潮间带"};
        String[] areas = {"雷州湾", "北部湾", "厦门湾", "舟山近海", "三亚湾", "珠江口", "渤海湾", "胶州湾", "闽江口", "汕头近岸"};
        String[] dominantSpecies = {
                "秋茄、白骨壤、弹涂鱼", "鹿角珊瑚、蝶鱼、隆头鱼", "鳗草、海马、小型甲壳类",
                "招潮蟹、芦苇、底栖贝类", "石斑鱼、真鲷、海藻群落", "牡蛎、藤壶、潮间带鱼类"
        };
        String[] habitatFeatures = {
                "潮间带泥质底，耐盐植物群落明显", "硬珊瑚覆盖集中，海水透明度较高", "浅海软底质，海草连片分布",
                "淡咸水交汇，沉积物细颗粒丰富", "受季风与潮流共同影响，生境异质性较高", "潮沟发育明显，底栖生物活跃"
        };
        String[] protectionLevels = {"省级重点保护", "国家级保护海域", "市级保护区域", "国家湿地公园", "海洋生态红线区", "地方重点修复区"};
        String[] threats = {"养殖扰动、岸线开发", "旅游踩踏、海水升温", "港口活动、富营养化", "围填海、淡水来量变化", "近岸污染、渔业干扰", "外来物种入侵、垃圾沉积"};
        double[][] centers = {
                {110.352100, 20.026500}, {109.508600, 18.247900}, {118.145800, 24.429200},
                {122.180000, 29.950000}, {109.620000, 18.280000}, {113.750000, 22.550000},
                {117.820000, 38.950000}, {120.380000, 36.120000}, {119.620000, 26.080000}, {116.760000, 23.360000}
        };

        List<Ecosystem> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            int typeIndex = i % types.length;
            int areaIndex = i % areas.length;
            double[] center = centers[areaIndex];
            Ecosystem item = new Ecosystem();
            item.setId(ECOSYSTEM_START_ID + i);
            item.setName(areas[areaIndex] + types[typeIndex] + "样区" + (i + 1));
            item.setEcosystemType(types[typeIndex]);
            item.setSeaArea(areas[areaIndex]);
            item.setLongitude(BigDecimal.valueOf(center[0] + (i % 3) * 0.015));
            item.setLatitude(BigDecimal.valueOf(center[1] + (i % 4) * 0.012));
            item.setAvgDepth(BigDecimal.valueOf(2.5 + (i % 12) * 1.3));
            item.setSalinityRange((12 + i % 8) + "-" + (26 + i % 10) + "‰");
            item.setTemperatureRange((18 + i % 5) + "-" + (28 + i % 4) + "℃");
            item.setDominantSpecies(dominantSpecies[typeIndex]);
            item.setHabitatFeature(habitatFeatures[typeIndex]);
            item.setProtectionLevel(protectionLevels[typeIndex]);
            item.setThreatFactors(threats[typeIndex]);
            item.setDescription("位于" + areas[areaIndex] + "的" + types[typeIndex] + "生态系统监测样区，适合作为物种多样性、群落结构、环境因子与观测活动的长期测试数据。");
            list.add(item);
        }
        ecosystemService.saveOrUpdateBatch(list);
    }

    private void seedSpecies() {
        if (hasAllSeedIds(speciesService, SPECIES_START_ID)) {
            return;
        }

        String[][] baseSpecies = {
                {"中华白海豚", "Sousa chinensis", "脊索动物门", "哺乳纲", "鲸偶蹄目", "海豚科", "Sousa", "chinensis", "体色灰白至粉白，吻部细长", "近岸活动，常小群出现", "珠江口、雷州湾、北部湾沿海", "国家一级", "易危"},
                {"鹿角珊瑚", "Acropora formosa", "刺胞动物门", "珊瑚纲", "石珊瑚目", "鹿角珊瑚科", "Acropora", "formosa", "分枝明显，外形似鹿角", "依赖透明海水和稳定光照", "海南三亚近岸珊瑚礁区", "无危", "近危"},
                {"鳗草", "Zostera marina", "被子植物门", "单子叶植物纲", "泽泻目", "鳗草科", "Zostera", "marina", "叶片带状，浅海成片生长", "适应浅海弱浪环境", "厦门湾、胶州湾海草床区域", "无危", "无危"},
                {"弹涂鱼", "Periophthalmus cantonensis", "脊索动物门", "辐鳍鱼纲", "鲈形目", "虾虎鱼科", "Periophthalmus", "cantonensis", "可短时离水活动，胸鳍发达", "常见于潮滩泥面觅食", "雷州湾红树林潮滩区域", "无危", "无危"},
                {"绿海龟", "Chelonia mydas", "脊索动物门", "爬行纲", "龟鳖目", "海龟科", "Chelonia", "mydas", "背甲光滑，四肢呈鳍状", "常在近海和海草床活动", "南海近岸海域", "国家一级", "濒危"},
                {"海马", "Hippocampus trimaculatus", "脊索动物门", "辐鳍鱼纲", "海龙目", "海龙科", "Hippocampus", "trimaculatus", "体型弯曲，头部似马", "常栖息于海草床和藻场", "福建、广东近海", "国家二级", "易危"},
                {"招潮蟹", "Uca arcuata", "节肢动物门", "甲壳纲", "十足目", "沙蟹科", "Uca", "arcuata", "雄蟹一侧螯足特别发达", "潮间带洞居、集群活动", "河口潮滩与红树林区域", "无危", "无危"},
                {"石斑鱼", "Epinephelus awoara", "脊索动物门", "辐鳍鱼纲", "鲈形目", "鮨科", "Epinephelus", "awoara", "体型粗壮，斑纹明显", "多栖于礁区和岩礁海底", "南海与东海近岸礁区", "无危", "无危"},
                {"海参", "Apostichopus japonicus", "棘皮动物门", "海参纲", "楯手目", "刺参科", "Apostichopus", "japonicus", "体表具肉刺，呈圆筒状", "底栖缓慢活动", "黄渤海沿岸", "国家二级", "易危"},
                {"海胆", "Strongylocentrotus intermedius", "棘皮动物门", "海胆纲", "正形目", "球海胆科", "Strongylocentrotus", "intermedius", "体外具棘刺", "附着岩礁，取食藻类", "北方近海岩礁区", "无危", "无危"},
                {"真鲷", "Pagrus major", "脊索动物门", "辐鳍鱼纲", "鲈形目", "鲷科", "Pagrus", "major", "体呈椭圆形，体色偏红", "常见于近岸岩礁和海湾", "东海与南海沿岸", "无危", "无危"},
                {"海月水母", "Aurelia aurita", "刺胞动物门", "钵水母纲", "旗口水母目", "海月水母科", "Aurelia", "aurita", "伞体透明，生殖腺清晰可见", "漂浮生活，受洋流影响", "中国多地沿海海域", "无危", "无危"}
        };

        double[][] centers = {
                {110.35, 20.03},
                {109.51, 18.24},
                {118.15, 24.43},
                {121.85, 30.10},
                {113.75, 22.55},
                {119.62, 26.08},
                {120.38, 36.12},
                {117.82, 38.95},
                {114.28, 22.62},
                {122.18, 29.95}
        };

        List<Species> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            String[] base = baseSpecies[i % baseSpecies.length];
            double[] center = centers[i % centers.length];
            Species item = new Species();
            item.setId(SPECIES_START_ID + i);
            item.setChineseName(base[0] + "样本" + String.format("%02d", i + 1));
            item.setLatinName(base[1]);
            item.setPhylum(base[2]);
            item.setKlass(base[3]);
            item.setOrder(base[4]);
            item.setFamily(base[5]);
            item.setGenus(base[6]);
            item.setSpecies(base[7]);
            item.setFeatures(base[8]);
            item.setHabits(base[9]);
            item.setDistribution(base[10]);
            item.setProtectLevel(base[11]);
            item.setEndangeredStatus(base[12]);
            item.setLongitude(BigDecimal.valueOf(center[0] + (i % 6) * 0.08));
            item.setLatitude(BigDecimal.valueOf(center[1] + (i % 5) * 0.05));
            item.setImageUrl("");
            item.setVideoUrl("");
            list.add(item);
        }
        speciesService.saveOrUpdateBatch(list);
    }

    private void ensureVisibleSpeciesPoints() {
        long count = speciesService.lambdaQuery()
                .isNotNull(Species::getLongitude)
                .isNotNull(Species::getLatitude)
                .count();
        if (count > 0) {
            return;
        }
        List<Species> list = Arrays.asList(
                buildMapSpecies(MAP_SPECIES_START_ID, "中华白海豚", "Sousa chinensis", "110.420500", "20.045300"),
                buildMapSpecies(MAP_SPECIES_START_ID + 1, "鹿角珊瑚", "Acropora formosa", "109.512300", "18.238600"),
                buildMapSpecies(MAP_SPECIES_START_ID + 2, "鳗草", "Zostera marina", "118.150200", "24.432100"),
                buildMapSpecies(MAP_SPECIES_START_ID + 3, "弹涂鱼", "Periophthalmus cantonensis", "110.348900", "20.029800")
        );
        speciesService.saveOrUpdateBatch(list);
    }

    private Species buildMapSpecies(long id, String chineseName, String latinName, String longitude, String latitude) {
        Species species = new Species();
        species.setId(id);
        species.setChineseName(chineseName);
        species.setLatinName(latinName);
        species.setDistribution("系统启动自动补齐的地图展示点位");
        species.setLongitude(new BigDecimal(longitude));
        species.setLatitude(new BigDecimal(latitude));
        return species;
    }

    private void seedObservations() {
        if (hasAllSeedIds(observationService, OBSERVATION_START_ID)) {
            return;
        }

        List<Long> ecosystemIds = ecosystemService.list().stream()
                .map(Ecosystem::getId)
                .filter(id -> id != null)
                .sorted()
                .collect(Collectors.toList());
        if (ecosystemIds.isEmpty()) {
            return;
        }

        String[] observers = {"admin", "researcher_a", "researcher_b", "student_a", "student_b"};
        String[] remarks = {
                "现场能见度较好，记录到多类海洋生物活动",
                "潮间带样线调查中发现典型物种聚集",
                "受风浪影响较小，适合继续开展长期监测",
                "局部水温偏高，建议后续关注季节变化",
                "海草/藻类覆盖较明显，幼体活动频繁"
        };

        double[][] centers = {
                {110.35, 20.03},
                {109.51, 18.24},
                {118.15, 24.43},
                {121.85, 30.10},
                {113.75, 22.55},
                {119.62, 26.08},
                {120.38, 36.12},
                {117.82, 38.95},
                {114.28, 22.62},
                {122.18, 29.95}
        };

        List<Observation> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            double[] center = centers[i % centers.length];
            Observation item = new Observation();
            item.setId(OBSERVATION_START_ID + i);
            item.setObsTime(LocalDateTime.now().minusDays(60L - i).withHour(8 + (i % 9)).withMinute((i * 7) % 60));
            item.setLongitude(BigDecimal.valueOf(center[0] + (i % 4) * 0.03));
            item.setLatitude(BigDecimal.valueOf(center[1] + (i % 3) * 0.025));
            item.setEcosystemId(ecosystemIds.get(i % ecosystemIds.size()));
            item.setObserver(observers[i % observers.length]);
            item.setWaterTemp(BigDecimal.valueOf(21.5 + (i % 10) * 0.7));
            item.setSalinity(BigDecimal.valueOf(18.0 + (i % 12) * 1.1));
            item.setRemark(remarks[i % remarks.length]);
            item.setCreatorId(1L);
            list.add(item);
        }
        observationService.saveOrUpdateBatch(list);
    }

    private void seedObservationSpecies() {
        if (hasAllSeedIds(observationSpeciesService, OBSERVATION_SPECIES_START_ID)) {
            return;
        }

        List<Long> observationIds = observationService.list().stream()
                .map(Observation::getId)
                .filter(id -> id != null)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        List<Long> speciesIds = speciesService.list().stream()
                .map(Species::getId)
                .filter(id -> id != null)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        if (observationIds.isEmpty() || speciesIds.isEmpty()) {
            return;
        }

        List<String> behaviors = Arrays.asList(
                "近岸巡游",
                "礁区停留",
                "群体觅食",
                "潮滩活动",
                "成片分布",
                "缓慢游动",
                "受惊散开",
                "底栖停留"
        );

        List<ObservationSpecies> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            ObservationSpecies item = new ObservationSpecies();
            item.setId(OBSERVATION_SPECIES_START_ID + i);
            item.setObservationId(observationIds.get(i % observationIds.size()));
            item.setSpeciesId(speciesIds.get(i % speciesIds.size()));
            item.setQuantity(1 + (i % 18));
            item.setBehavior(behaviors.get(i % behaviors.size()));
            list.add(item);
        }
        observationSpeciesService.saveOrUpdateBatch(list);
    }

    private <T> boolean hasAllSeedIds(com.baomidou.mybatisplus.extension.service.IService<T> service, long startId) {
        for (long id = startId; id < startId + SEED_SIZE; id++) {
            if (service.getById(id) == null) {
                return false;
            }
        }
        return true;
    }
}