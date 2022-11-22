## b
$$
1 + 4 + 4^2 + ... + 4^{k-1} = (4^{k-1} - 1) / 3
$$
Let $T(k)$ be the time needed to solve a problem of size `k`, where `k` is the number of hours one is responsible for the pump. For the recursive algorithm I implemented, in the worst case, $T(k)$ is solved by solving four sub-problems:

- If full service is performed, then $T(k-4)$ is to be solved, plus some constant time.
- If regular service is performed, then $T(k-2)$ is to be solved, plus some constant time.
- If minor service is performed, then $T(k-1)$ is to be solved, plus some constant time.
- If no service is performed, then $T(k-1)$ is to be solved, plus some constant time.

Therefore, we have
$$
T(k) = 2*T(k-1) + T(k-2) + T(k-4) + C_0 \\
\geq 4*T(k-4) + C_1 \\
= 4*T(k-8) + 4 * C_1 + C_1 \\
= ... \\
= C_1*(4^{k/4-1} + 4^{k/4-2} + ... + 4 + 1)
= C_1 * (4^{k/4-1} - 1) / 3
$$

Then  $T(k) \geq \Omega(C_1 * (4^{k/4-1} - 1) / 3) = \Omega(4^{k/4})$. The asymptotic lower bound is $\Omega(4^{k/4})$.
It is exponential because with this recursive search method, a significant portion of the nodes expands to 4 child nodes and the size of the search tree is therefore exponential in terms of `k`.



## d

The dynamic programming solution implemented fills up a table of size $k * (k / 2) * 3$. For each cell, calculations use constant time. Therefore, the asymptotic upper bound is $O(k * (k / 2) * 3) = O(k^2)$.
