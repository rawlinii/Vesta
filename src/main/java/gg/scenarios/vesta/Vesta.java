package gg.scenarios.vesta;

import com.google.gson.Gson;
import gg.scenarios.vesta.announcer.Announcer;
import gg.scenarios.vesta.commands.*;
import gg.scenarios.vesta.database.Redis;
import gg.scenarios.vesta.exploits.CustomPayloadFixer;
import gg.scenarios.vesta.listeners.PlayerListener;
import gg.scenarios.vesta.managers.ServerManager;
import gg.scenarios.vesta.managers.tags.Tag;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Getter
public class Vesta extends JavaPlugin {

    @Getter
    public static Vesta instance;
    @Getter
    public ServerManager serverManager;
    @Getter
    private Permission perms = null;
    @Getter
    private Chat chat = null;
    @Getter
    private Gson gson = new Gson();
    @Getter
    private MongoClientURI clientURI;
    @Getter
    private MongoClient mongoClient;
    @Getter
    private MongoDatabase mongoDatabase;
    @Getter
    private MongoCollection<Document> profiles;
    @Getter
    private MongoCollection<Document> tags;
    @Getter
    private Redis redis;

    @Override
    public void onEnable() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        instance = this;
        saveDefaultConfig();
        saveConfig();
        serverManager = new ServerManager();
        registerEvents();
        registerCommands();
        setupPermissions();
        setupChat();
        setupMongo();
        redis = new Redis(this);
        new Announcer(this);
        new CustomPayloadFixer(this);
    }


    private void setupMongo() {
        String uri = getConfig().getString("server.mongo.uri");
        clientURI = new MongoClientURI(uri);
        mongoClient = new MongoClient(clientURI);
        mongoDatabase = mongoClient.getDatabase((serverManager.isDev()) ? "Vesta_dev" : "Vesta_main");
        profiles = mongoDatabase.getCollection("profiles");
        tags = mongoDatabase.getCollection("tags");
        Tag.load();
    }


    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private void registerCommands() {
        getCommand("color").setExecutor(new ColorCommand());
        getCommand("message").setExecutor(new MessageCommand());
        getCommand("reply").setExecutor(new ReplyCommand());
        getCommand("tags").setExecutor(new TagsCommand());
        getCommand("tagadmin").setExecutor(new TagAdminCommand());
        getCommand("gamemode").setExecutor(new GameModeCommand(this));
        getCommand("staffchat").setExecutor(new StaffChatCommand(this));
        getCommand("announce").setExecutor(new AnnounceCommand(this));
        getCommand("discord").setExecutor(new DiscordCommand(this));
        getCommand("hub").setExecutor(new HubCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
