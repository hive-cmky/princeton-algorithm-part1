package com.princeton.partone.module2;

class UnionFind {
    private int[] parent;
    private int[] size;
    private int count;  // number of components

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        count = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    // Find with path compression
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // path compression
        }
        return parent[x];
    }

    // Union by size
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;  // Already connected
        }

        // Attach smaller tree to larger tree
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }

        count--;
        return true;
    }

    public boolean isFullyConnected() {
        return count == 1;
    }

    public int getCount() {
        return count;
    }
}

class Friendship {
    int timestamp;
    int member1;
    int member2;

    public Friendship(int timestamp, int member1, int member2) {
        this.timestamp = timestamp;
        this.member1 = member1;
        this.member2 = member2;
    }
}

class SocialNetworkConnectivity {

    /**
     * Finds the earliest timestamp when all members are connected
     * @param n number of members (0 to n-1)
     * @param friendships list of friendships sorted by timestamp
     * @return earliest timestamp when all connected, or -1 if never connected
     */
    public static int findEarliestConnectionTime(int n, Friendship[] friendships) {
        UnionFind uf = new UnionFind(n);

        for (Friendship friendship : friendships) {
            uf.union(friendship.member1, friendship.member2);

            if (uf.isFullyConnected()) {
                return friendship.timestamp;
            }
        }

        return -1;  // Not all members connected
    }

    // Test example
    public static void main(String[] args) {
        int n = 5;  // 5 members: 0, 1, 2, 3, 4

        Friendship[] friendships = {
                new Friendship(1, 0, 1),  // t=1: 0-1 become friends
                new Friendship(2, 2, 3),  // t=2: 2-3 become friends
                new Friendship(3, 0, 2),  // t=3: 0-2 become friends (connects {0,1} with {2,3})
                new Friendship(4, 1, 4),  // t=4: 1-4 become friends (connects everyone)
                new Friendship(5, 3, 4)   // t=5: 3-4 become friends (already connected)
        };

        int result = findEarliestConnectionTime(n, friendships);

        if (result != -1) {
            System.out.println("All members connected at timestamp: " + result);
        } else {
            System.out.println("Not all members are connected");
        }

        // Additional test: Show component count evolution
        System.out.println("\nComponent evolution:");
        UnionFind uf = new UnionFind(n);
        System.out.println("Initial components: " + uf.getCount());

        for (Friendship f : friendships) {
            uf.union(f.member1, f.member2);
            System.out.println("After timestamp " + f.timestamp +
                    " (connecting " + f.member1 + "-" + f.member2 +
                    "): " + uf.getCount() + " components");
        }
    }
}
