package com.example.omnimarket.Screens;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.omnimarket.DB.AppDataBase;
import com.example.omnimarket.DB.ShopDAO;
import com.example.omnimarket.Item;
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.StoreBinding;

import java.util.ArrayList;
import java.util.List;

public class ShopScreen extends AppCompatActivity {
    StoreBinding binding;
    ShopDAO mShopDAO;

    TextView mShopWelcome;
    LinearLayout mItemDisplay;
    ScrollView mScrollView;

AutoCompleteTextView mAutoCompleteTextView;
    Button mHome;

    List<Item> mItemList;
    User mReturnedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store);

        binding = StoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mShopWelcome = binding.shopWelcome;
        mItemDisplay = binding.itemDisplay;//findViewById(R.id.item_display);
        mScrollView = binding.scrollShop;
        mHome = binding.shopHomeButton;
        mAutoCompleteTextView = binding.searchDropDown;
        mReturnedUser = (User) getIntent().getSerializableExtra("USER_KEY");

        mShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .ShopDAO();

        checkForItems();
        ArrayAdapter<Item> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, mItemList);
        mAutoCompleteTextView.setAdapter(adapter);
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            Item item = (Item) parent.getItemAtPosition(position);
            displayItem(item);
            // For example, you can access the properties of the selected item

        });

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = HomeScreen.getIntent(getApplicationContext(), mReturnedUser);
                intent.putExtra("USER_KEY", mReturnedUser);
                startActivity(intent);
            }
        });
        //checkForItems();


    }// END OF ONCREATE

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context, ShopScreen.class);
        return intent;
    }

    private void checkForItems(){
        mItemList = mShopDAO.getItems();
        if (mItemList.isEmpty()){
            for (Item item: startingItems){
                mShopDAO.insert(item);
            }
            mItemList = mShopDAO.getItems();
        }

    }

    public void handleOrder(Item item){
        item.setUserID(mReturnedUser.getUserID());
        item.reduceQuantity();
        mShopDAO.update(item);
        Toast.makeText(this, item.getName() + " purchased!", Toast.LENGTH_SHORT).show();
    }

    public void displayItem(Item item){
        mItemDisplay.removeAllViews();
        TextView mItemView = new TextView(this);
        mItemView.setWidth(900);
        mItemView.setTextSize(15);
        mItemView.setText(item.toString());
        mItemDisplay.addView(mItemView);

        Button mOrderButt = new Button(this);
        mOrderButt.setText("Order");
        if (item.getQuantity() > 0){
            mItemDisplay.addView(mOrderButt);
        }

        mItemView.append("---------------------------------------------------\n");

        mOrderButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOrder(item);
                displayItem(item);
            }
        });
        mAutoCompleteTextView.setText("");
    }


        //Toast.makeText(this, search, Toast.LENGTH_SHORT).show();
    Item testItem = new Item("test Item", 10, 0, "this is a test item", -1);
    Item testItem2 = new Item("test Item 2", 5, 1, "this is a second test item", -1);
    Item supermanCape = new Item("Superman's Cape", 999.99, 1, "A legendary cape worn by the Man of Steel himself, obtained from the Fortress of Solitude", -1);
    Item anakinLightsaber = new Item("Anakin Skywalker's Lightsaber", 799.99, 1, "The lightsaber once wielded by the Chosen One, retrieved from the ruins of the Jedi Temple", -1);
    Item spockEars = new Item("Spock's Vulcan Ears", 59.99, 1, "Authentic Vulcan ears worn by the iconic Starfleet officer, acquired from a rare intergalactic auction", -1);
    Item drWhoSonicScrewdriver = new Item("Doctor Who's Sonic Screwdriver", 149.99, 1, "The iconic tool of the Time Lord, discovered in the depths of the TARDIS", -1);
    Item leiaHolographicMessage = new Item("Leia's Holographic Message", 299.99, 1, "A holographic message from Princess Leia, recovered from a hidden Rebel Alliance base", -1);
    Item millenniumFalconModel = new Item("Millennium Falcon Model", 499.99, 1, "A detailed model of the fastest ship in the galaxy, acquired from a renowned space collector", -1);
    Item tronIdentityDisk = new Item("Tron's Identity Disk", 129.99, 1, "The identity disk used in the digital world of Tron, obtained during a virtual reality expedition", -1);
    Item bladeRunnerGun = new Item("Blade Runner's Deckard Gun", 199.99, 1, "The iconic firearm used by Rick Deckard, acquired from the gritty streets of Los Angeles 2049", -1);
    Item predatorMask = new Item("Predator's Mask", 349.99, 1, "The fearsome mask of the Yautja warrior, claimed as a trophy after a close encounter", -1);
    Item babylon5Uniform = new Item("Babylon 5 Starfury Pilot Uniform", 179.99, 1, "A genuine Starfury pilot uniform worn in the defense of Babylon 5, obtained from Earthforce archives", -1);
    Item lightsaberUmbrella = new Item("Lightsaber Umbrella", 29.99, 1, "An umbrella with a hilt that lights up like a lightsaber, acquired from a futuristic city market", -1);
    Item walleRobotReplica = new Item("WALL-E Robot Replica", 599.99, 1, "A detailed replica of WALL-E, the waste-collecting robot, salvaged from the remains of a spaceship", -1);
    Item cyberpunkNeuralImplant = new Item("Cyberpunk Neural Implant", 199.99, 1, "A cutting-edge neural implant from the cyberpunk era, procured from an underground augmentation clinic", -1);
    Item tardisKey = new Item("TARDIS Key", 39.99, 1, "The key to the TARDIS, obtained from a mysterious wanderer who travels through time and space", -1);
    Item expanseRociModel = new Item("The Expanse Roci Model", 129.99, 1, "A miniature model of the Rocinante spaceship from The Expanse, acquired at a space enthusiasts convention", -1);
    Item starTrekCommunicator = new Item("Star Trek Communicator", 89.99, 1, "A replica of the communicator from Star Trek, beamed in from a parallel universe during a temporal anomaly", -1);
    Item stargateGlyphDecoder = new Item("Stargate Glyph Decoder", 79.99, 1, "A device to decode Stargate glyphs, discovered in an archaeological dig near a buried Stargate", -1);
    Item battlestarGalacticaModel = new Item("Battlestar Galactica Model", 179.99, 1, "A detailed model of the Battlestar Galactica, recovered from the wreckage of the final battle", -1);
    Item alienXenomorphEggReplica = new Item("Alien Xenomorph Egg Replica", 299.99, 1, "A chillingly realistic replica of an Alien Xenomorph egg, acquired from a deep-space research facility", -1);
    Item marvelInfinityGauntlet = new Item("Marvel Infinity Gauntlet", 999.99, 1, "A powerful gauntlet with all six Infinity Stones, found in an ancient cosmic temple", -1);
    Item pipBoy3000 = new Item("Pip-Boy 3000", 149.99, 1, "A functional wrist-mounted computer from the Fallout universe, scavenged from a post-apocalyptic bunker", -1);
    Item masterSwordReplica = new Item("Master Sword Replica", 79.99, 1, "A replica of Link's iconic Master Sword from The Legend of Zelda, found in the ruins of the Temple of Time", -1);
    Item portalGun = new Item("Portal Gun", 499.99, 1, "A handheld device allowing teleportation between portals, acquired from the Aperture Science Enrichment Center", -1);
    Item doomBFG = new Item("DOOM BFG (Big F***ing Gun)", 699.99, 1, "A massive energy weapon from DOOM, salvaged from the remains of a demon-infested research facility on Mars", -1);
    Item deusExNanoAugmentations = new Item("Deus Ex Nano Augmentations", 299.99, 1, "Cutting-edge nanotechnological augmentations from Deus Ex, acquired from an underground augmentation clinic", -1);
    Item marioMushroom = new Item("Super Mario Mushroom", 9.99, 1, "A magical mushroom that grants incredible growth, found in the secret passages of the Mushroom Kingdom", -1);
    Item elderScrollsAmulet = new Item("Elder Scrolls Amulet", 129.99, 1, "An enchanted amulet from The Elder Scrolls, discovered in the depths of an ancient Dwemer ruin", -1);
    Item bioshockPlasmids = new Item("BioShock Plasmids", 199.99, 1, "Genetic enhancements that grant superhuman abilities, obtained from the underwater city of Rapture", -1);
    Item assassinCreedHiddenBlade = new Item("Assassin's Creed Hidden Blade", 89.99, 1, "A functional replica of the Assassin's hidden blade, acquired from a skilled blade-wielding assassin", -1);
    Item haloEnergySword = new Item("Halo Energy Sword", 299.99, 1, "A Covenant energy sword from the Halo universe, retrieved from the battlefield on Reach", -1);
    Item cyberpunkHoverboard = new Item("Cyberpunk Hoverboard", 149.99, 1, "A levitating hoverboard from the cyberpunk future, acquired from a high-tech black market", -1);
    Item assassinsCreedHiddenBladeReplica = new Item("Assassin's Creed Hidden Blade Replica", 59.99, 1, "A detailed replica of the Assassin's hidden blade, crafted by a skilled artisan", -1);
    Item starWarsHolocron = new Item("Star Wars Holocron", 79.99, 1, "An ancient Jedi or Sith Holocron containing vast knowledge of the Force, discovered in a forgotten temple", -1);
    Item elderScrollsSweetroll = new Item("Elder Scrolls Sweetroll", 9.99, 1, "A delicious sweetroll from The Elder Scrolls, procured from a bakery in Whiterun", -1);
    Item falloutNukaColaQuantum = new Item("Fallout Nuka-Cola Quantum", 19.99, 1, "A rare and irradiated Nuka-Cola Quantum, salvaged from the remains of a Fallout shelter", -1);
    Item haloMasterChiefHelmet = new Item("Halo Master Chief Helmet", 299.99, 1, "A wearable replica of Master Chief's iconic helmet, obtained from a UNSC armory", -1);
    Item massEffectOmniTool = new Item("Mass Effect Omni-Tool", 129.99, 1, "A versatile omni-tool from the Mass Effect universe, acquired from a tech-savvy asari engineer", -1);
    Item witcherMedallion = new Item("Witcher's Medallion", 49.99, 1, "A medallion worn by Witchers, enchanted to detect the presence of monsters, found in the ruins of Kaer Morhen", -1);
    Item halfLifeGravityGun = new Item("Half-Life Gravity Gun", 199.99, 1, "The experimental Zero-Point Energy Field Manipulator, recovered from the Black Mesa Research Facility", -1);
    Item finalFantasyPhoenixDown = new Item("Final Fantasy Phoenix Down", 29.99, 1, "A magical feather that can revive fallen allies, obtained from a chocobo's nest in the world of Final Fantasy", -1);
    Item bioshockVigors = new Item("BioShock Vigors", 179.99, 1, "Bottled genetic modifications granting extraordinary abilities, found in the floating city of Columbia", -1);
    Item destinyGhostShell = new Item("Destiny Ghost Shell", 89.99, 1, "A Ghost companion from the Destiny universe, discovered on the Moon during a lunar expedition", -1);
    Item borderlandsPsychoMask = new Item("Borderlands Psycho Mask", 69.99, 1, "A replica of the Psycho bandit mask from Borderlands, acquired from a bandit-infested wasteland", -1);
    Item warframeExcaliburStatue = new Item("Warframe Excalibur Statue", 399.99, 1, "A collector's edition statue of the Warframe Excalibur, obtained from a Tenno relics auction", -1);
    Item metalGearSolidBox = new Item("Metal Gear Solid Cardboard Box", 39.99, 1, "A classic cardboard box used for stealth in Metal Gear Solid, sourced from a military surplus store", -1);
    Item overwatchPayloadReplica = new Item("Overwatch Payload Replica", 149.99, 1, "A miniature replica of an Overwatch payload, taken from the aftermath of a heated payload escort mission", -1);
    Item minecraftDiamondSword = new Item("Minecraft Diamond Sword", 24.99, 1, "A pixelated diamond sword from Minecraft, crafted by a skilled player in the pixelated world", -1);
    Item starCraftTerranMarineHelmet = new Item("StarCraft Terran Marine Helmet", 159.99, 1, "A wearable helmet worn by Terran Marines in StarCraft, obtained from a surplus Dominion armory", -1);
    Item titanfallBT7274Helmet = new Item("Titanfall BT-7274 Helmet", 499.99, 1, "A full-scale helmet worn by the Titan BT-7274, recovered from the wreckage of a Vanguard-class Titan", -1);
    Item fortniteLlamaPlush = new Item("Fortnite Llama Plush", 19.99, 1, "A plush llama from Fortnite, won in a victory royale and delivered by airdrop", -1);
    Item inceptionTotem = new Item("Inception Totem", 49.99, 1, "A mysterious totem used to determine reality from dreams, obtained from the dreamscape of Cobb's team", -1);
    Item backToTheFutureHoverboard = new Item("Back to the Future Hoverboard", 129.99, 1, "Marty McFly's iconic hoverboard from the future, acquired from a vintage shop in Hill Valley", -1);
    Item starTrekTricorder = new Item("Star Trek Tricorder", 89.99, 1, "A handheld device for scanning and data analysis from Star Trek, acquired from a Federation outpost", -1);
    Item matrixRedPill = new Item("Matrix Red Pill", 19.99, 1, "The red pill from The Matrix, obtained from Morpheus to awaken from the simulated reality", -1);
    Item bladeRunnerSpinner = new Item("Blade Runner Spinner", 299.99, 1, "A flying car from Blade Runner, acquired from the futuristic streets of Los Angeles 2019", -1);
    Item etSpaceshipReplica = new Item("E.T.'s Spaceship Replica", 69.99, 1, "A miniature replica of E.T.'s spaceship, obtained from a secret government storage facility", -1);
    Item ghostbustersProtonPack = new Item("Ghostbusters Proton Pack", 199.99, 1, "A replica of the Ghostbusters' proton pack, obtained from Ray's Occult Books", -1);
    Item wallEPlantSeedling = new Item("WALL-E Plant Seedling", 9.99, 1, "A small plant seedling from Earth, preserved by WALL-E, acquired from the Axiom's archives", -1);
    Item interstellarEnduranceModel = new Item("Interstellar Endurance Model", 179.99, 1, "A detailed model of the spaceship Endurance from Interstellar, obtained from NASA's archives", -1);
    Item starWarsDeathStarBlueprint = new Item("Star Wars Death Star Blueprint", 399.99, 1, "Blueprints detailing the construction of the Death Star, acquired from Rebel Alliance intelligence", -1);

    List<Item> startingItems = List.of(
            supermanCape,
            anakinLightsaber,
            spockEars,
            drWhoSonicScrewdriver,
            leiaHolographicMessage,
            millenniumFalconModel,
            tronIdentityDisk,
            bladeRunnerGun,
            predatorMask,
            babylon5Uniform,
            lightsaberUmbrella,
            walleRobotReplica,
            cyberpunkNeuralImplant,
            tardisKey,
            expanseRociModel,
            starTrekCommunicator,
            stargateGlyphDecoder,
            battlestarGalacticaModel,
            alienXenomorphEggReplica,
            marvelInfinityGauntlet,
            pipBoy3000,
            masterSwordReplica,
            portalGun,
            doomBFG,
            deusExNanoAugmentations,
            marioMushroom,
            elderScrollsAmulet,
            bioshockPlasmids,
            assassinCreedHiddenBlade,
            haloEnergySword,
            cyberpunkHoverboard,
            assassinsCreedHiddenBladeReplica,
            starWarsHolocron,
            elderScrollsSweetroll,
            falloutNukaColaQuantum,
            haloMasterChiefHelmet,
            massEffectOmniTool,
            witcherMedallion,
            halfLifeGravityGun,
            finalFantasyPhoenixDown,
            bioshockVigors,
            destinyGhostShell,
            borderlandsPsychoMask,
            warframeExcaliburStatue,
            metalGearSolidBox,
            overwatchPayloadReplica,
            minecraftDiamondSword,
            starCraftTerranMarineHelmet,
            titanfallBT7274Helmet,
            fortniteLlamaPlush,
            inceptionTotem,
            backToTheFutureHoverboard,
            starTrekTricorder,
            matrixRedPill,
            bladeRunnerSpinner,
            etSpaceshipReplica,
            ghostbustersProtonPack,
            wallEPlantSeedling,
            interstellarEnduranceModel,
            starWarsDeathStarBlueprint
    );
}