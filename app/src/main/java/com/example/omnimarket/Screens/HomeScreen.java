package com.example.omnimarket.Screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omnimarket.DB.AppDataBase;
import com.example.omnimarket.DB.ShopDAO;
import com.example.omnimarket.Item;
import com.example.omnimarket.Purchase;
import com.example.omnimarket.R;
import com.example.omnimarket.User;
import com.example.omnimarket.databinding.HomepageBinding;

import java.util.List;

public class HomeScreen extends AppCompatActivity {
    HomepageBinding binding;

    private static final String USER_ID_KEY = "com.example.omnimarket.USER_ID_KEY";
    TextView mWelcomeMessage;

    Button mPastOrders;
    Button mFind;
    Button mLogout;
    Button mAdmin;
    Button mDelete;

    ShopDAO mShopDAO;
    User mReturnedUser;
    List<Purchase> mPurchaseList;
    List <Item> mItemList;
    Item mItem;
    ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        binding = HomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mWelcomeMessage = binding.welcomeMessage;
        mConstraintLayout = binding.getRoot();
        mPastOrders = binding.pastOrdersButton;
        mFind = binding.findButton;
        mLogout = binding.logoutButton;
        mAdmin = binding.adminModeButton;
        mDelete = binding.deleteUserButton;
        mReturnedUser = (User) getIntent().getSerializableExtra("USER_KEY");

        mShopDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .ShopDAO();

        checkForItems();


        //BUTTON CLICK FUNCTIONS BELOW
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginScreen.getIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        mFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ShopScreen.getIntent(getApplicationContext());
                intent.putExtra("USER_KEY", mReturnedUser);
                startActivity(intent);
            }
        });

        mPastOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = PastOrders.getIntent((getApplicationContext()), mReturnedUser);
                intent.putExtra("USER_KEY", mReturnedUser);
                startActivity(intent);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDialog(mReturnedUser);
            }
        });

        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminMenu.getIntent(getApplicationContext(), mReturnedUser);
                intent.putExtra("USER_KEY", mReturnedUser);
                startActivity(intent);
            }
        });

        checkAdmin();
    }// END OF ONCREATE

    public static Intent getIntent(Context context, User user){
        Intent intent = new Intent(context, HomeScreen.class);
        intent.putExtra(USER_ID_KEY, user.getUserID());
        return intent;
    }

    public void confirmDeleteDialog(User user){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete user?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which){
                        removeItems();
                        mShopDAO.delete(user);
                        Toast.makeText(getApplicationContext(), "User Has Been Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = LoginScreen.getIntent(getApplicationContext());
                        startActivity(intent);
                    }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "User Will Not Be Deleted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                })
                .show();
    }

    public void removeItems(){
        mPurchaseList = mShopDAO.getPurchasesByUserId(mReturnedUser.getUserID());
        for (Purchase purchase: mPurchaseList){
            mItem = mShopDAO.getItemById(purchase.getItemID());
            mItem.setQuantity(mItem.getQuantity() + purchase.getQuantity());
            mShopDAO.update(mItem);
            mShopDAO.delete(purchase);
        }

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
    private void checkAdmin(){
        if (mReturnedUser.getAdmin() == true){
            mWelcomeMessage.append("Administrator " + mReturnedUser.getName());
            mAdmin.setVisibility(View.VISIBLE);
        } else if (mReturnedUser.getAdmin() == false) {
            mWelcomeMessage.append("" + mReturnedUser.getName());
        }
    }


    Item testItem = new Item("test Item", 10, 0, "this is a test item");
    Item testItem2 = new Item("test Item 2", 5, 1, "this is a second test item");
    Item supermanCape = new Item("Superman's Cape", 999.99, 3, "A legendary cape worn by the Man of Steel himself, obtained from the Fortress of Solitude");
    Item anakinLightsaber = new Item("Anakin Skywalker's Lightsaber", 799.99, 7, "The lightsaber once wielded by the Chosen One, retrieved from the ruins of the Jedi Temple");
    Item spockEars = new Item("Spock's Vulcan Ears", 59.99, 1, "Authentic Vulcan ears worn by the iconic Starfleet officer, acquired from a rare intergalactic auction");
    Item drWhoSonicScrewdriver = new Item("Doctor Who's Sonic Screwdriver", 149.99, 15, "The iconic tool of the Time Lord, discovered in the depths of the TARDIS");
    Item leiaHolographicMessage = new Item("Leia's Holographic Message", 299.99, 4, "A holographic message from Princess Leia, recovered from a hidden Rebel Alliance base");
    Item millenniumFalconModel = new Item("Millennium Falcon Model", 499.99, 17, "A detailed model of the fastest ship in the galaxy, acquired from a renowned space collector");
    Item tronIdentityDisk = new Item("Tron's Identity Disk", 129.99, 10, "The identity disk used in the digital world of Tron, obtained during a virtual reality expedition");
    Item bladeRunnerGun = new Item("Blade Runner's Deckard Gun", 199.99, 15, "The iconic firearm used by Rick Deckard, acquired from the gritty streets of Los Angeles 2049");
    Item predatorMask = new Item("Predator's Mask", 349.99, 2, "The fearsome mask of the Yautja warrior, claimed as a trophy after a close encounter");
    Item babylon5Uniform = new Item("Babylon 5 Starfury Pilot Uniform", 179.99, 11, "A genuine Starfury pilot uniform worn in the defense of Babylon 5, obtained from Earthforce archives");
    Item lightsaberUmbrella = new Item("Lightsaber Umbrella", 29.99, 12, "An umbrella with a hilt that lights up like a lightsaber, acquired from a futuristic city market");
    Item walleRobotReplica = new Item("WALL-E Robot Replica", 599.99, 11, "A detailed replica of WALL-E, the waste-collecting robot, salvaged from the remains of a spaceship");
    Item cyberpunkNeuralImplant = new Item("Cyberpunk Neural Implant", 199.99, 13, "A cutting-edge neural implant from the cyberpunk era, procured from an underground augmentation clinic");
    Item tardisKey = new Item("TARDIS Key", 39.99, 4, "The key to the TARDIS, obtained from a mysterious wanderer who travels through time and space");
    Item expanseRociModel = new Item("The Expanse Roci Model", 129.99, 6, "A miniature model of the Rocinante spaceship from The Expanse, acquired at a space enthusiasts convention");
    Item starTrekCommunicator = new Item("Star Trek Communicator", 89.99, 11, "A replica of the communicator from Star Trek, beamed in from a parallel universe during a temporal anomaly");
    Item stargateGlyphDecoder = new Item("Stargate Glyph Decoder", 79.99, 5, "A device to decode Stargate glyphs, discovered in an archaeological dig near a buried Stargate");
    Item battlestarGalacticaModel = new Item("Battlestar Galactica Model", 179.99, 11, "A detailed model of the Battlestar Galactica, recovered from the wreckage of the final battle");
    Item alienXenomorphEggReplica = new Item("Alien Xenomorph Egg Replica", 299.99, 10, "A chillingly realistic replica of an Alien Xenomorph egg, acquired from a deep-space research facility");
    Item marvelInfinityGauntlet = new Item("Marvel Infinity Gauntlet", 999.99, 1, "A powerful gauntlet with all six Infinity Stones, found in an ancient cosmic temple");
    Item pipBoy3000 = new Item("Pip-Boy 3000", 149.99, 12, "A functional wrist-mounted computer from the Fallout universe, scavenged from a post-apocalyptic bunker");
    Item masterSwordReplica = new Item("Master Sword Replica", 79.99, 13, "A replica of Link's iconic Master Sword from The Legend of Zelda, found in the ruins of the Temple of Time");
    Item portalGun = new Item("Portal Gun", 499.99, 12, "A handheld device allowing teleportation between portals, acquired from the Aperture Science Enrichment Center");
    Item doomBFG = new Item("DOOM BFG (Big F***ing Gun)", 699.99, 7, "A massive energy weapon from DOOM, salvaged from the remains of a demon-infested research facility on Mars");
    Item deusExNanoAugmentations = new Item("Deus Ex Nano Augmentations", 299.99, 12, "Cutting-edge nanotechnological augmentations from Deus Ex, acquired from an underground augmentation clinic");
    Item marioMushroom = new Item("Super Mario Mushroom", 9.99, 15, "A magical mushroom that grants incredible growth, found in the secret passages of the Mushroom Kingdom");
    Item elderScrollsAmulet = new Item("Elder Scrolls Amulet", 129.99, 12, "An enchanted amulet from The Elder Scrolls, discovered in the depths of an ancient Dwemer ruin");
    Item bioshockPlasmids = new Item("BioShock Plasmids", 199.99, 12, "Genetic enhancements that grant superhuman abilities, obtained from the underwater city of Rapture");
    Item assassinCreedHiddenBlade = new Item("Assassin's Creed Hidden Blade", 89.99, 11, "A functional replica of the Assassin's hidden blade, acquired from a skilled blade-wielding assassin");
    Item haloEnergySword = new Item("Halo Energy Sword", 299.99, 12, "A Covenant energy sword from the Halo universe, retrieved from the battlefield on Reach");
    Item cyberpunkHoverboard = new Item("Cyberpunk Hoverboard", 149.99, 13, "A levitating hoverboard from the cyberpunk future, acquired from a high-tech black market");
    Item assassinsCreedHiddenBladeReplica = new Item("Assassin's Creed Hidden Blade Replica", 59.99, 1, "A detailed replica of the Assassin's hidden blade, crafted by a skilled artisan");
    Item starWarsHolocron = new Item("Star Wars Holocron", 79.99, 13, "An ancient Jedi or Sith Holocron containing vast knowledge of the Force, discovered in a forgotten temple");
    Item elderScrollsSweetroll = new Item("Elder Scrolls Sweetroll", 9.99, 5, "A delicious sweetroll from The Elder Scrolls, procured from a bakery in Whiterun");
    Item falloutNukaColaQuantum = new Item("Fallout Nuka-Cola Quantum", 19.99, 5, "A rare and irradiated Nuka-Cola Quantum, salvaged from the remains of a Fallout shelter");
    Item haloMasterChiefHelmet = new Item("Halo Master Chief Helmet", 299.99, 3, "A wearable replica of Master Chief's iconic helmet, obtained from a UNSC armory");
    Item massEffectOmniTool = new Item("Mass Effect Omni-Tool", 129.99, 6, "A versatile omni-tool from the Mass Effect universe, acquired from a tech-savvy asari engineer");
    Item witcherMedallion = new Item("Witcher's Medallion", 49.99, 3, "A medallion worn by Witchers, enchanted to detect the presence of monsters, found in the ruins of Kaer Morhen");
    Item halfLifeGravityGun = new Item("Half-Life Gravity Gun", 199.99, 2, "The experimental Zero-Point Energy Field Manipulator, recovered from the Black Mesa Research Facility");
    Item finalFantasyPhoenixDown = new Item("Final Fantasy Phoenix Down", 29.99, 7, "A magical feather that can revive fallen allies, obtained from a chocobo's nest in the world of Final Fantasy");
    Item bioshockVigors = new Item("BioShock Vigors", 179.99, 3, "Bottled genetic modifications granting extraordinary abilities, found in the floating city of Columbia");
    Item destinyGhostShell = new Item("Destiny Ghost Shell", 89.99, 4, "A Ghost companion from the Destiny universe, discovered on the Moon during a lunar expedition");
    Item borderlandsPsychoMask = new Item("Borderlands Psycho Mask", 69.99, 2, "A replica of the Psycho bandit mask from Borderlands, acquired from a bandit-infested wasteland");
    Item warframeExcaliburStatue = new Item("Warframe Excalibur Statue", 399.99, 5, "A collector's edition statue of the Warframe Excalibur, obtained from a Tenno relics auction");
    Item metalGearSolidBox = new Item("Metal Gear Solid Cardboard Box", 39.99, 3, "A classic cardboard box used for stealth in Metal Gear Solid, sourced from a military surplus store");
    Item overwatchPayloadReplica = new Item("Overwatch Payload Replica", 149.99, 4, "A miniature replica of an Overwatch payload, taken from the aftermath of a heated payload escort mission");
    Item minecraftDiamondSword = new Item("Minecraft Diamond Sword", 24.99, 2, "A pixelated diamond sword from Minecraft, crafted by a skilled player in the pixelated world");
    Item starCraftTerranMarineHelmet = new Item("StarCraft Terran Marine Helmet", 159.99, 1, "A wearable helmet worn by Terran Marines in StarCraft, obtained from a surplus Dominion armory");
    Item titanfallBT7274Helmet = new Item("Titanfall BT-7274 Helmet", 499.99, 6, "A full-scale helmet worn by the Titan BT-7274, recovered from the wreckage of a Vanguard-class Titan");
    Item fortniteLlamaPlush = new Item("Fortnite Llama Plush", 19.99, 7, "A plush llama from Fortnite, won in a victory royale and delivered by airdrop");
    Item inceptionTotem = new Item("Inception Totem", 49.99, 3, "A mysterious totem used to determine reality from dreams, obtained from the dreamscape of Cobb's team");
    Item backToTheFutureHoverboard = new Item("Back to the Future Hoverboard", 129.99, 4, "Marty McFly's iconic hoverboard from the future, acquired from a vintage shop in Hill Valley");
    Item starTrekTricorder = new Item("Star Trek Tricorder", 89.99, 5, "A handheld device for scanning and data analysis from Star Trek, acquired from a Federation outpost");
    Item matrixRedPill = new Item("Matrix Red Pill", 19.99, 3, "The red pill from The Matrix, obtained from Morpheus to awaken from the simulated reality");
    Item bladeRunnerSpinner = new Item("Blade Runner Spinner", 299.99, 2, "A flying car from Blade Runner, acquired from the futuristic streets of Los Angeles 2019");
    Item etSpaceshipReplica = new Item("E.T.'s Spaceship Replica", 69.99, 5, "A miniature replica of E.T.'s spaceship, obtained from a secret government storage facility");
    Item ghostbustersProtonPack = new Item("Ghostbusters Proton Pack", 199.99, 1, "A replica of the Ghostbusters' proton pack, obtained from Ray's Occult Books");
    Item wallEPlantSeedling = new Item("WALL-E Plant Seedling", 9.99, 2, "A small plant seedling from Earth, preserved by WALL-E, acquired from the Axiom's archives");
    Item interstellarEnduranceModel = new Item("Interstellar Endurance Model", 179.99, 13, "A detailed model of the spaceship Endurance from Interstellar, obtained from NASA's archives");
    Item starWarsDeathStarBlueprint = new Item("Star Wars Death Star Blueprint", 399.99, 12, "Blueprints detailing the construction of the Death Star, acquired from Rebel Alliance intelligence");

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