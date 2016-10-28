#include <stdio.h>
#include <unistd.h>

extern char **environ;

int main(int argc, char *argv[])
{

	/* If less than two arguments (argv[0] -> program, argv[1] -> file to save environment) print an error y return -1 */
	if(argc < 2)
	{
		printf("Too few arguments\n");
		return -1;
	}

	return 0;
}
