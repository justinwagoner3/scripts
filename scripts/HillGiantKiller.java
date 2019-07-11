package scripts;

import Tasks.*;
import Tasks.Traversal.BankToHillGiants;
import Tasks.Traversal.HillGiantsToBank;
import Tasks.Traversal.TraverseAtoB;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.powerbot.script.rt4.Constants.*;

//TODO the problem is the areas im using, they are overlapping

@Script.Manifest(name="Hill Giant Killer", description="Kills Hill Giants in Edgeville Dungeon", properties = "author=justinwagoner3; topic=1352224; client=4;")

public class HillGiantKiller extends PollingScript<ClientContext> implements PaintListener, MessageListener {

    private ArrayList<Task> taskList;
    private int startXP;
    public int xpGained;
    //public TraverseAtoB traverseHillGiantsToBank;
    //public TraverseAtoB traverseBankToHillGiants;


    public HillGiantKiller(){
        taskList = new ArrayList<Task>();
        startXP = ctx.skills.experience(SKILLS_STRENGTH) + ctx.skills.experience(SKILLS_ATTACK) + ctx.skills.experience(SKILLS_DEFENSE);
        xpGained = 0;
        //traverseHillGiantsToBank = new TraverseAtoB(ctx,HILL_GIANTS_TO_VARROACK_WEST_BANK,HILL_GIANTS_TO_VARROACK_WEST_BANK,((ctx.inventory.select().id(FOOD_ID).count() < 1) && (!VARROCK_WEST_BANK_AREA.contains(ctx.players.local()))), false);
        //traverseBankToHillGiants = new TraverseAtoB(ctx,HILL_GIANTS_TO_VARROACK_WEST_BANK,HILL_GIANTS_TO_VARROACK_WEST_BANK,(ctx.npcs.select().id(MONSTER_ID).isEmpty() && ctx.inventory.select().id(FOOD_ID).count() > 1), true);
        //traverseBankToHillGiants = new TraverseAtoB(ctx,HILL_GIANTS_TO_VARROACK_WEST_BANK,HILL_GIANTS_TO_VARROACK_WEST_BANK,ctx.inventory.select().id(FOOD_ID).count() > 1, true); // one argument still doesnt work
        //traverseBankToHillGiants = new TraverseAtoB(ctx,HILL_GIANTS_TO_VARROACK_WEST_BANK,HILL_GIANTS_TO_VARROACK_WEST_BANK, evaluateCondition(), true);

    }


    public void start(){
        System.out.println("Starting");
        // TODO add back in Hop Worlds
        taskList.addAll(Arrays.asList(new HopWorlds(ctx, 7), new Attack(ctx), new Heal(ctx), new RunActiveChecker(ctx), new AntiBan(ctx), new BankingWithdraw(ctx), new HillGiantsToBank(ctx), new BankToHillGiants(ctx)));
    }

    public void stop(){
        System.out.println("Stopping");
    }

    public void poll(){
        for (Task task : taskList) {
            if (task.activate())
                task.execute();
        }

    }

    @Override
    public void messaged(MessageEvent messageEvent) {

    }

    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    @Override
    public void repaint(Graphics graphics) {
        xpGained = ctx.skills.experience(SKILLS_STRENGTH) + ctx.skills.experience(SKILLS_ATTACK) + ctx.skills.experience(SKILLS_DEFENSE) - startXP;
        final int xpGainedHr = (int) ((xpGained * 3600000D) / getRuntime());
        long runTime = getRuntime() / 1000; // gets it in seconds
        int hours = (int) runTime / 3600;
        runTime -= hours * 3600;
        int minutes = (int) runTime / 60;
        int seconds = (int) runTime % 60;


        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);
        g.setColor(Color.black);
        g.fillRoundRect(1, 1, 180, 50, 20, 20);
        g.setColor(Color.CYAN);
        g.drawString(String.format("Jwagg's Hill Giant Killer"), 10, 15);

        g.setColor(Color.WHITE);
        g.drawString(String.format("Runtime: " + hours + ":" + minutes + ":" + seconds), 10, 30);
        g.drawString(String.format("Xp Ganied: %,d (%,d)", xpGained, xpGainedHr), 10, 45);

    }
}
