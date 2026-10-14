package raffle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

/**
 * Runs one raffle: sells tickets, holds prizes, draws winners, and can undo
 * the last draw. Every rejected action throws an exception and leaves the
 * raffle unchanged.
 *
 * @author Group 54
 * @version 2026.10.14
 */
public class Raffle {

    private static final String SEP = "\t";

    private LinkedChain<Ticket> tickets;
    private LinkedChain<Prize> prizes;
    private LinkedChain<Winner> winners;
    private Stack<Winner> drawHistory;
    // Not final so load() can replace it; nothing else ever changes it.
    private int ticketPriceCents;
    private int totalRaisedCents;
    private int nextTicketId;
    private final Random random;

    /**
     * Starts an empty raffle.
     *
     * @param ticketPriceCents
     *            the price of one ticket in cents
     * @param random
     *            the source of randomness for draws
     * @throws IllegalArgumentException
     *             if the price is not positive or random is null
     */
    public Raffle(int ticketPriceCents, Random random) {
        if (ticketPriceCents <= 0) {
            throw new IllegalArgumentException(
                "Ticket price must be more than 0 cents.");
        }
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null.");
        }
        this.ticketPriceCents = ticketPriceCents;
        this.random = random;
        tickets = new LinkedChain<Ticket>();
        prizes = new LinkedChain<Prize>();
        winners = new LinkedChain<Winner>();
        drawHistory = new Stack<Winner>();
        totalRaisedCents = 0;
        nextTicketId = 1;
    }


    /**
     * Sells count tickets to the named person.
     *
     * @param name
     *            the buyer's name
     * @param count
     *            how many tickets to sell
     * @throws IllegalArgumentException
     *             if the name is blank, count is not positive, or the total
     *             would be too large to store
     */
    public void sellTickets(String name, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException(
                "Number of tickets must be at least 1.");
        }
        Entrant owner = findEntrant(name);
        if (owner == null) {
            owner = new Entrant(name);
        }
        int newTotal;
        try {
            newTotal = Math.addExact(totalRaisedCents, Math.multiplyExact(
                count, ticketPriceCents));
            Math.addExact(nextTicketId, count);
        }
        catch (ArithmeticException e) {
            throw new IllegalArgumentException("That is too many tickets.");
        }
        for (int i = 0; i < count; i++) {
            tickets.add(new Ticket(nextTicketId, owner));
            nextTicketId++;
        }
        totalRaisedCents = newTotal;
    }


    /**
     * Adds one prize to the raffle.
     *
     * @param name
     *            the prize name
     * @throws IllegalArgumentException
     *             if the name is blank
     */
    public void addPrize(String name) {
        prizes.add(new Prize(name));
    }


    /**
     * Counts the tickets owned by the named person.
     *
     * @param name
     *            the person's name
     * @return how many tickets they hold, or 0 if none
     */
    public int getTicketCount(String name) {
        String key = Entrant.normalize(name);
        int count = 0;
        for (Object o : tickets.toArray()) {
            if (((Ticket)o).getOwner().getKey().equals(key)) {
                count++;
            }
        }
        return count;
    }


    /**
     * Draws a random ticket, gives it the next prize, and records the winner.
     *
     * @return the new winner
     * @throws IllegalStateException
     *             if there are no tickets or no prizes
     */
    public Winner drawWinner() {
        if (tickets.isEmpty()) {
            throw new IllegalStateException("There are no tickets to draw.");
        }
        if (prizes.isEmpty()) {
            throw new IllegalStateException("There are no prizes left.");
        }
        Ticket ticket = tickets.getEntryAt(random.nextInt(tickets
            .getCurrentSize()));
        Prize prize = prizes.remove();
        tickets.remove(ticket);
        Winner winner = new Winner(ticket, prize);
        winners.add(winner);
        drawHistory.push(winner);
        return winner;
    }


    /**
     * Undoes the most recent draw, putting its ticket and prize back.
     *
     * @return the winner that was undone
     * @throws IllegalStateException
     *             if there is no draw to undo
     */
    public Winner undoLastDraw() {
        if (drawHistory.isEmpty()) {
            throw new IllegalStateException("There is no draw to undo.");
        }
        Winner last = drawHistory.pop();
        tickets.add(last.getTicket());
        prizes.add(last.getPrize());
        winners.remove(last);
        return last;
    }


    /**
     * Refunds count of the named person's tickets.
     *
     * @param name
     *            the person's name
     * @param count
     *            how many tickets to refund
     * @throws IllegalArgumentException
     *             if count is not positive or the person has fewer than count
     *             tickets
     */
    public void refund(String name, int count) {
        if (count <= 0) {
            throw new IllegalArgumentException(
                "Number of tickets to refund must be at least 1.");
        }
        int owned = getTicketCount(name);
        if (count > owned) {
            throw new IllegalArgumentException("That person has only "
                + owned + " ticket(s) to refund.");
        }
        String key = Entrant.normalize(name);
        for (int i = 0; i < count; i++) {
            tickets.remove(findTicket(key));
        }
        totalRaisedCents -= count * ticketPriceCents;
    }


    /**
     * Returns the named person's chance of winning the next draw.
     *
     * @param name
     *            the person's name
     * @return their tickets divided by all tickets, or 0.0 if there are none
     */
    public double getOdds(String name) {
        if (tickets.isEmpty()) {
            return 0.0;
        }
        return (double)getTicketCount(name) / tickets.getCurrentSize();
    }


    /**
     * Writes the raffle to a text file, one record per line.
     *
     * @param fileName
     *            the file to write
     * @throws IllegalStateException
     *             if the file cannot be written
     */
    public void save(String fileName) {
        try (PrintWriter out = new PrintWriter(new FileWriter(fileName))) {
            out.println("PRICE" + SEP + ticketPriceCents);
            out.println("RAISED" + SEP + totalRaisedCents);
            out.println("NEXTID" + SEP + nextTicketId);
            for (Object o : tickets.toArray()) {
                Ticket t = (Ticket)o;
                out.println("TICKET" + SEP + t.getId() + SEP + t.getOwner()
                    .getDisplayName());
            }
            for (Object o : prizes.toArray()) {
                out.println("PRIZE" + SEP + ((Prize)o).getName());
            }
            // Winners are listed newest first, like the chain.
            for (Object o : winners.toArray()) {
                Winner w = (Winner)o;
                out.println("WINNER" + SEP + w.getTicket().getId() + SEP + w
                    .getEntrant().getDisplayName() + SEP + w.getPrize()
                        .getName());
            }
            if (out.checkError()) {
                throw new IOException("write failed");
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not save to " + fileName
                + ".");
        }
    }


    /**
     * Replaces all of this raffle's state with what a file written by save
     * holds. If anything goes wrong the old state is kept.
     *
     * @param fileName
     *            the file to read
     * @throws IllegalStateException
     *             if the file is missing or not in the save format
     */
    public void load(String fileName) {
        Stack<Ticket> ticketList = new Stack<Ticket>();
        Stack<Prize> prizeList = new Stack<Prize>();
        Stack<Winner> winnerList = new Stack<Winner>();
        Map<String, Entrant> entrants = new HashMap<String, Entrant>();
        int price = -1;
        int raised = -1;
        int nextId = -1;
        try (BufferedReader in = new BufferedReader(new FileReader(
            fileName))) {
            String line = in.readLine();
            while (line != null) {
                if (!line.isEmpty()) {
                    String[] parts = line.split(SEP, 4);
                    switch (parts[0]) {
                        case "PRICE":
                            price = Integer.parseInt(parts[1]);
                            break;
                        case "RAISED":
                            raised = Integer.parseInt(parts[1]);
                            break;
                        case "NEXTID":
                            nextId = Integer.parseInt(parts[1]);
                            break;
                        case "TICKET":
                            ticketList.push(new Ticket(Integer.parseInt(
                                parts[1]), entrantFor(parts[2], entrants)));
                            break;
                        case "PRIZE":
                            prizeList.push(new Prize(line.substring(6)));
                            break;
                        case "WINNER":
                            winnerList.push(new Winner(new Ticket(Integer
                                .parseInt(parts[1]), entrantFor(parts[2],
                                    entrants)), new Prize(parts[3])));
                            break;
                        default:
                            throw new IllegalArgumentException(line);
                    }
                }
                line = in.readLine();
            }
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not read " + fileName
                + ".");
        }
        catch (RuntimeException e) {
            throw new IllegalStateException(fileName
                + " is not a saved raffle.");
        }
        if (price <= 0 || raised < 0 || nextId < 1) {
            throw new IllegalStateException(fileName
                + " is not a saved raffle.");
        }

        // Each list was read front first; add from the back so the chains
        // come out in the same order they were saved in.
        ticketPriceCents = price;
        totalRaisedCents = raised;
        nextTicketId = nextId;
        tickets.clear();
        while (!ticketList.isEmpty()) {
            tickets.add(ticketList.pop());
        }
        prizes.clear();
        while (!prizeList.isEmpty()) {
            prizes.add(prizeList.pop());
        }
        winners.clear();
        drawHistory.clear();
        while (!winnerList.isEmpty()) {
            Winner w = winnerList.pop();
            winners.add(w);
            drawHistory.push(w);
        }
    }


    /**
     * Returns the owner of the first ticket whose owner matches name.
     *
     * @param name
     *            the name to look for
     * @return the matching entrant, or null if none
     */
    private Entrant findEntrant(String name) {
        Ticket ticket = findTicket(Entrant.normalize(name));
        return ticket == null ? null : ticket.getOwner();
    }


    /**
     * Returns the first ticket whose owner has the given key, or null.
     */
    private Ticket findTicket(String key) {
        for (Object o : tickets.toArray()) {
            Ticket t = (Ticket)o;
            if (t.getOwner().getKey().equals(key)) {
                return t;
            }
        }
        return null;
    }


    /**
     * Returns the shared Entrant for name, creating it the first time it is
     * seen while loading.
     */
    private static Entrant entrantFor(String name, Map<String, Entrant> seen) {
        String key = Entrant.normalize(name);
        Entrant entrant = seen.get(key);
        if (entrant == null) {
            entrant = new Entrant(name);
            seen.put(key, entrant);
        }
        return entrant;
    }


    /**
     * @return how many tickets are still in the draw
     */
    public int getRemainingTicketCount() {
        return tickets.getCurrentSize();
    }


    /**
     * @return how many prizes are left to award
     */
    public int getRemainingPrizeCount() {
        return prizes.getCurrentSize();
    }


    /**
     * @return the total money raised, in cents
     */
    public int getTotalRaisedCents() {
        return totalRaisedCents;
    }


    /**
     * @return the price of one ticket, in cents
     */
    public int getTicketPriceCents() {
        return ticketPriceCents;
    }


    /**
     * @return a snapshot of the remaining tickets
     */
    public Object[] getTickets() {
        return tickets.toArray();
    }


    /**
     * @return a snapshot of the remaining prizes, next prize first
     */
    public Object[] getPrizes() {
        return prizes.toArray();
    }


    /**
     * @return a snapshot of the winners, most recent first
     */
    public Object[] getWinners() {
        return winners.toArray();
    }
}
