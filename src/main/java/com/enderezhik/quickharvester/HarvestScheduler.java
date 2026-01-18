package com.enderezhik.quickharvester;

import java.util.HashMap;
import java.util.UUID;
import com.enderezhik.quickharvester.tasks.IHarvestTask;

public class HarvestScheduler {
    public static final HashMap<UUID, IHarvestTask> tasks = new HashMap<>();
}
